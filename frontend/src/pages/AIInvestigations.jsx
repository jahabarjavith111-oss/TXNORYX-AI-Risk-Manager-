import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { getAnalysis } from "../services/riskService";
import { getFraudAnalysis } from "../services/fraudService";
import { getTransaction } from "../services/transactionService";
import { getAgentActivity, executeAgent } from "../services/agentService";
import TransactionDetails from "../components/TransactionDetails";
import AIInsight from "../components/AIInsight";
import RecoveryPanel from "../components/RecoveryPanel";
import AgentTimeline from "../components/AgentTimeline";

function AIInvestigations() {
  const { transactionId } = useParams();
  const [analysis, setAnalysis] = useState(null);
  const [fraud, setFraud] = useState(null);
  const [transaction, setTransaction] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!transactionId) return;
    setLoading(true);
    Promise.allSettled([
      getAnalysis(transactionId),
      getFraudAnalysis(transactionId),
      getTransaction(transactionId),
    ]).then(([a, f, t]) => {
      if (a.status === "fulfilled") setAnalysis(a.value);
      else setError("Could not load AI analysis");
      if (f.status === "fulfilled") setFraud(f.value);
      if (t.status === "fulfilled") setTransaction(t.value);
      setLoading(false);
    });
  }, [transactionId]);

  if (loading) {
    return (
      <div style={{ padding: "24px", textAlign: "center", color: "#64748b" }}>
        <p>🧠 AI analyzing transaction...</p>
        <div style={{ marginTop: "12px", color: "#94a3b8" }}>Loading transaction details…</div>
      </div>
    );
  }

  if (!transaction) {
    return (
      <div style={{ padding: "24px", textAlign: "center", color: "#64748b" }}>
        <p>📄 Loading transaction…</p>
        <div style={{ marginTop: "12px", color: "#94a3b8" }}>Fetching from backend…</div>
      </div>
    );
  }

  if (error && !analysis) {
    return (
      <div style={{ padding: "24px", textAlign: "center", color: "#ef4444" }}>
        <p>⚠ Unable to load analysis for {transactionId}</p>
        <Link to="/transactions" style={{ color: "#2b84ea" }}>← Back to Transactions</Link>
      </div>
    );
  }

  const isCritical = fraud?.riskLevel === "CRITICAL" || analysis?.riskLevel === "CRITICAL";
  const showRecovery = !isCritical && transaction && ["FAILED", "TIMEOUT", "DECLINED"].includes(transaction.status);

  return (
    <div style={{ padding: "20px", maxWidth: "960px", margin: "0 auto", display: "flex", flexDirection: "column", gap: 16 }}>
      <nav style={{ padding: "10px 0" }}>
        <Link style={{ color: "#2b84ea", textDecoration: "none", fontWeight: 600, fontSize: 13 }} to="/transactions">
          ← Back to Transactions
        </Link>
      </nav>

      <TransactionDetails transaction={transaction} fraud={fraud} loading={loading} />

      <AIInsight analysis={analysis} loading={false} error={error} />

      {isCritical && (
        <div style={{ background: "rgba(239,68,68,0.08)", borderRadius: 10, padding: "16px", border: "1px solid #fecaca" }}>
          <div style={{ color: "#ef4444", fontWeight: 800, marginBottom: 6 }}>🚨 CRITICAL RISK — BLOCK TRANSACTION</div>
          <div style={{ fontSize: 12, color: "#64748b" }}>TXNORYX will not auto-recover suspicious payments. Escalate for manual review.</div>
          <div style={{ marginTop: 10, display: "flex", gap: 8 }}>
            <button onClick={() => window.history.back()} style={{ padding: "6px 14px", borderRadius: 6, border: "1px solid #e2e8f0", background: "#fff", fontSize: 12, cursor: "pointer" }}>VIEW DETAILS</button>
            <button onClick={() => executeAgent(transactionId).then(() => alert("Escalated"))} style={{ padding: "6px 14px", borderRadius: 6, border: "none", background: "#f97316", color: "#fff", fontWeight: 600, fontSize: 12, cursor: "pointer" }}>ESCALATE</button>
          </div>
        </div>
      )}

      {showRecovery ? (
        <RecoveryPanel transactionId={transactionId} onComplete={() => window.dispatchEvent(new CustomEvent("txns:changed"))} />
      ) : (!isCritical && transaction ? (
        <div style={{ background: "rgba(255,255,255,0.95)", borderRadius: 10, padding: "16px", border: "1px solid #e2e8f0", textAlign: "center", color: "#64748b", fontSize: 12 }}>
          No recovery needed — transaction status is {transaction.status}.
        </div>
      ) : null)}

      <AgentTimeline activeStep={analysis ? 6 : 3} />

      <div style={{ background: "rgba(255,255,255,0.95)", borderRadius: 10, padding: "14px", border: "1px solid #e2e8f0" }}>
        <h4 style={{ margin: "0 0 8px 0", fontSize: 12, color: "#0f172a" }}>INITIATED → GATEWAY TIMEOUT → RISK ANALYSIS → AI DECISION → ROUTE SWITCH → RETRY → SUCCESS</h4>
        <div style={{ display: "grid", gridTemplateColumns: "repeat(4, 1fr)", gap: 8, fontSize: 11 }}>
          <div style={{ background: "#f8fafc", borderRadius: 6, padding: 8, textAlign: "center" }}><div style={{ color: "#94a3b8" }}>Risk Score</div><strong>24</strong></div>
          <div style={{ background: "#f8fafc", borderRadius: 6, padding: 8, textAlign: "center" }}><div style={{ color: "#94a3b8" }}>Fraud</div><strong style={{ color: "#22c55e" }}>4%</strong></div>
          <div style={{ background: "#f8fafc", borderRadius: 6, padding: 8, textAlign: "center" }}><div style={{ color: "#94a3b8" }}>Recovery</div><strong style={{ color: "#22c55e" }}>91%</strong></div>
          <div style={{ background: "#f8fafc", borderRadius: 6, padding: 8, textAlign: "center" }}><div style={{ color: "#94a3b8" }}>Confidence</div><strong style={{ color: "#2b84ea" }}>94%</strong></div>
        </div>
        <div style={{ fontSize: 11, color: "#334155", background: "#f8fafc", borderRadius: 6, padding: "8px 10px", marginTop: 8 }}>AI Reasoning: Temporary gateway failure detected. Transaction history is normal. Fraud probability is low. Alternative route has high success rate. Recommended action: <strong style={{ color: "#2b84ea" }}>SWITCH ROUTE + RETRY</strong></div>
      </div>

      <div style={{ textAlign: "center", fontSize: 11, color: "#94a3b8" }}>
        TXN-10482 · INITIATED → PROCESSING → FAILED → ANALYZING → AI_DECISION → RECOVERY → SUCCESS — governed autonomy
      </div>
    </div>
  );
}

export default AIInvestigations;
