package com.txnoryx.backend.ai;

import com.txnoryx.backend.model.AIAnalysis;
import com.txnoryx.backend.model.Transaction;
import com.txnoryx.backend.risk.RiskEngine;
import com.txnoryx.backend.risk.RiskResult;
import com.txnoryx.backend.repository.TransactionRepository;
import com.txnoryx.backend.repository.AIAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

@Service
public class AIAnalysisService {

    private final TransactionRepository transactionRepository;
    private final AIAnalysisRepository aiAnalysisRepository;
    private final RiskEngine riskEngine;

    // Ollama config
    private static final String OLLAMA_URL = "http://localhost:11434/api/generate";
    private static final String OLLAMA_MODEL = "qwen3.5:latest";

public AIAnalysisService(TransactionRepository transactionRepository,
                         AIAnalysisRepository aiAnalysisRepository,
                         RiskEngine riskEngine) {
        this.transactionRepository = transactionRepository;
        this.aiAnalysisRepository = aiAnalysisRepository;
        this.riskEngine = riskEngine;
    }

    @Transactional
    public AIAnalysis analyzeTransaction(String transactionId) {

        // 1️⃣ Fetch the actual transaction from the database
        Transaction transaction = transactionRepository
                .findByTransactionId(transactionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Transaction not found: " + transactionId));

        // 2️⃣ Compute deterministic risk engine result
        RiskResult engineResult = riskEngine.calculate(transaction);

        // 3️⃣ Build the AI prompt per spec §9-§10
        String prompt = String.format(
                "You are TXNORYX, an autonomous payment risk intelligence system." +
                        "\n\n" +
                        "Analyze the transaction below and return ONLY valid JSON with these fields: riskLevel, confidence, rootCause, recommendation, explanation." +
                        "\n\n" +
                        "Transaction ID: %s" +
                        "\nAmount: %.2f INR" +
                        "\nPayment Method: %s" +
                        "\nMerchant: %s" +
                        "\nStatus: %s" +
                        "\nFailure Reason: %s" +
                        "\nDevice: %s" +
                        "\nLocation: %s" +
                        "\nCalculated Risk Score: %d" +
                        "\nCalculated Risk Level: %s" +
                        "\nRisk Factors: %s" +
                        "\nDo not invent transaction information. Use only the supplied data." +
                        "\nForce the AI to return valid JSON.",
                        transactionId,
                        transaction.getAmount() != null ? transaction.getAmount().doubleValue() : 0.0,
                        transaction.getPaymentMethod() != null ? transaction.getPaymentMethod() : "—",
                        transaction.getMerchant() != null ? transaction.getMerchant() : "—",
                        transaction.getStatus() != null ? transaction.getStatus() : "—",
                        transaction.getFailureReason() != null ? transaction.getFailureReason() : "—",
                        transaction.getDeviceId() != null ? transaction.getDeviceId() : "—",
                        transaction.getLocation() != null ? transaction.getLocation() : "—",
                        engineResult.getScore(),
                        engineResult.getLevel().name(),
                        engineResult.getReason());

        // 4️⃣ Attempt AI investigation via Ollama
        AIAnalysis analysis = new AIAnalysis();
        analysis.setTransactionId(transactionId);

        // Default to engine result as fallback
        analysis.setRiskScore(engineResult.getScore());
        analysis.setRiskLevel(engineResult.getLevel().name());
        analysis.setConfidence(0.8);
        analysis.setRootCause(engineResult.getReason());
        analysis.setRecommendation("RETRY_PAYMENT");
        analysis.setExplanation("The transaction appears to be affected by " + engineResult.getReason().toLowerCase() + " rather than clear fraudulent behavior.");
        analysis.setCreatedAt(LocalDateTime.now());

        // Try Ollama
        try {
            URL url = new URL(OLLAMA_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String escapedPrompt = prompt.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
            String jsonInput = "{\"model\":\"" + OLLAMA_MODEL +
                    "\",\"format\":\"json\",\"prompt\":\"" + escapedPrompt + "\",\"stream\":false}";

            byte[] input = jsonInput.getBytes("UTF-8");
            conn.getOutputStream().write(input);

            int responseCode = conn.getResponseCode();
            StringBuilder response = new StringBuilder();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse the AI response - extract JSON fields
                String aiResponse = response.toString();
                // Simple extraction (in production use Jackson)
                String riskLevel = extractJsonField(aiResponse, "riskLevel");
                String confidenceStr = extractJsonField(aiResponse, "confidence");
                String rootCause = extractJsonField(aiResponse, "rootCause");
                String recommendation = extractJsonField(aiResponse, "recommendation");
                String explanation = extractJsonField(aiResponse, "explanation");

                if (riskLevel != null && !riskLevel.isEmpty()) {
                    analysis.setRiskLevel(riskLevel);
                }
                if (confidenceStr != null && !confidenceStr.isEmpty()) {
                    try {
                        analysis.setConfidence(Double.parseDouble(confidenceStr));
                    } catch (NumberFormatException e) {
                        analysis.setConfidence(0.8);
                    }
                } else {
                    analysis.setConfidence(0.8);
                }
                if (rootCause != null && !rootCause.isEmpty()) {
                    analysis.setRootCause(rootCause);
                } else {
                    analysis.setRootCause(engineResult.getReason());
                }
                if (recommendation != null && !recommendation.isEmpty()) {
                    analysis.setRecommendation(recommendation);
                } else {
                    analysis.setRecommendation("RETRY_PAYMENT");
                }
                if (explanation != null && !explanation.isEmpty()) {
                    analysis.setExplanation(explanation);
                } else {
                    analysis.setExplanation("The transaction appears to be affected by " + engineResult.getReason().toLowerCase() + " rather than clear fraudulent behavior.");
                }
            } else {
                // Non-200 response - engine result stands
            }
            conn.disconnect();
        } catch (Exception e) {
            // AI unavailable - engine result stands with engine confidence
            // confidence already set to 0.8 above
        }

        // 5️⃣ Persist to DB
        aiAnalysisRepository.save(analysis);
        return analysis;
    }

    /** Simple JSON field extraction for Day 4 (Jackon not added as dep yet). */
    private static String extractJsonField(String json, String fieldName) {
        String marker = "\"" + fieldName + "\":";
        int idx = json.indexOf(marker);
        if (idx < 0) return null;
        int start = idx + marker.length();
        int end = json.indexOf(",", start);
        if (end < 0) end = json.indexOf("}", start);
        if (end < 0) return null;
        return json.substring(start, end).trim().replace("\"", "");
    }
}