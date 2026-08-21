import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { getFraudAnalysis, analyzeFraud } from "../services/fraudService";

const RISK_COLORS = {
  LOW: "#22c55e",
  MEDIUM: "#f59e0b",
  HIGH: "#f97316",
  CRITICAL: "#ef4444",
};

function FraudDetection() {
  const [transactionId, setTransactionId] = useState("txn-013");
  const [analysis, setAnalysis] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const runAnalysis = async (id) => {
    if (!id.trim()) return;
    setLoading(true);
    setError(null);
    try {
      const result = await analyzeFraud(id.trim());
      setAnalysis(result);
    } catch (err) {
      setError("Could not analyze transaction");
      setAnalysis(null);
    }
    setLoading(false);
  };

  useEffect(() => {
    runAnalysis(transactionId);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <div style={{ padding: "24px", maxWidth: "900px", margin: "0 auto" }}>
      <h2 style={{ marginBottom: "6px" }}>Fraud Detection Engine</h2>
      <p style={{ color: "#64748b", marginBottom: "20px" }}>
        Multi-factor fraud scoring: high amount, suspicious status, velocity & gateway failures.
      </p>

      <div style={{ display: "flex", gap: "10px", marginBottom: "24px" }}>
        <input
          value={transactionId}
          onChange={(e) => setTransactionId(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && runAnalysis(transactionId)}
          placeholder="Enter transaction ID e.g. txn-013"
          style={{
            flex: 1,
            padding: "12px 14px",
            borderRadius: 8,
            border: "1px solid #cbd5e1",
            fontSize: 14,
            outline: "none",
          }}
        />
        <button
          onClick={() => runAnalysis(transactionId)}
          disabled={loading}
          style={{
            padding: "12px 22px",
            borderRadius: 8,
            border: "none",
            background: loading ? "#94a3b8" : "#667eea",
            color: "#fff",
            fontWeight: 600,
            cursor: loading ? "wait" : "pointer",
            fontSize: 14,
          }}
        >
          {loading ? "Analyzing…" : "Analyze"}
        </button>
      </div>

      {error && (
        <div style={{ padding: "16px", borderRadius: 8, background: "rgba(239,68,68,0.1)", color: "#ef4444", marginBottom: "20px" }}>
          {error}
        </div>
      )}

      {loading && !analysis && <p style={{ color: "#64748b" }}>Running fraud analysis…</p>}

      {analysis && (
        <div>
          {/* Summary cards */}
          <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(160px, 1fr))", gap: "14px", marginBottom: "22px" }}>
            <div style={cardStyle}>
              <div style={{ fontSize: 28, fontWeight: 700, color: RISK_COLORS[analysis.riskLevel] || "#667eea" }}>
                {analysis.riskScore}
              </div>
              <div style={{ fontSize: 12, color: "#64748b" }}>Risk Score /100</div>
            </div>
            <div style={cardStyle}>
              <div style={{ fontSize: 22, fontWeight: 700, color: RISK_COLORS[analysis.riskLevel] || "#667eea" }}>
                {analysis.riskLevel}
              </div>
              <div style={{ fontSize: 12, color: "#64748b" }}>Risk Level</div>
            </div>
            <div style={cardStyle}>
              <div
                style={{
                  fontSize: 18,
                  fontWeight: 700,
                  padding: "4px 12px",
                  borderRadius: 6,
                  display: "inline-block",
                  background: analysis.suspicious ? "rgba(239,68,68,0.15)" : "rgba(34,197,94,0.15)",
                  color: analysis.suspicious ? "#ef4444" : "#22c55e",
                }}
              >
                {analysis.suspicious ? "SUSPICIOUS" : "CLEAN"}
              </div>
              <div style={{ fontSize: 12, color: "#64748b", marginTop: 6 }}>Verdict</div>
            </div>
            <div style={cardStyle}>
              <div style={{ fontSize: 15, fontWeight: 600, color: "#3182ce" }}>{analysis.recommendation}</div>
              <div style={{ fontSize: 12, color: "#64748b" }}>Recommendation</div>
            </div>
          </div>

          {/* Factors */}
          <h3 style={{ marginBottom: "12px" }}>Detected Risk Factors ({analysis.factors?.length ?? 0})</h3>
          {(analysis.factors?.length ?? 0) === 0 ? (
            <p style={{ color: "#22c55e", fontWeight: 600 }}>No risk factors detected — transaction appears legitimate.</p>
          ) : (
            <div style={{ display: "flex", flexDirection: "column", gap: "10px" }}>
              {analysis.factors.map((f, i) => (
                <div key={i} style={{ display: "flex", alignItems: "center", gap: "14px", background: "rgba(255,255,255,0.85)", borderRadius: 8, padding: "14px 16px" }}>
                  <span style={{ minWidth: 42, textAlign: "center", fontWeight: 700, color: "#fff", background: scoreColor(f.score), borderRadius: 6, padding: "5px 8px", fontSize: 13 }}>
                    +{f.score}
                  </span>
                  <div>
                    <div style={{ fontWeight: 700, fontSize: 13, color: "#334155" }}>{f.factor}</div>
                    <div style={{ fontSize: 12, color: "#64748b" }}>{f.explanation}</div>
                  </div>
                </div>
              ))}
            </div>
          )}

          <p style={{ marginTop: "22px", fontSize: 12, color: "#94a3b8" }}>
            Transaction <strong>{analysis.transactionId}</strong> analyzed by TXNORYX Fraud Detection Engine v1.0
          </p>
        </div>
      )}
    </div>
  );
}

const cardStyle = {
  background: "rgba(255,255,255,0.9)",
  borderRadius: 10,
  padding: "18px",
  textAlign: "center",
  boxShadow: "0 1px 3px rgba(0,0,0,0.06)",
};

function scoreColor(score) {
  if (score >= 30) return "#ef4444";
  if (score >= 20) return "#f97316";
  if (score >= 10) return "#f59e0b";
  return "#22c55e";
}

export default FraudDetection;
