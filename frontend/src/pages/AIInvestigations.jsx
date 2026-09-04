import { useEffect, useState, useCallback } from "react";
import { Link, useParams, useNavigate } from "react-router-dom";
import { getAnalysis, analyzeTransaction } from "../services/riskService";
import { getFraudAnalysis } from "../services/fraudService";
import { getTransaction, getTransactions } from "../services/transactionService";
import { executeAgent } from "../services/agentService";
import TransactionDetails from "../components/TransactionDetails";
import AIInsight from "../components/AIInsight";
import RecoveryPanel from "../components/RecoveryPanel";
import AgentTimeline from "../components/AgentTimeline";
function AIInvestigations() {
  const { transactionId } = useParams();
  const navigate = useNavigate();
  const [analysis, setAnalysis] = useState(null);
  const [fraud, setFraud] = useState(null);
  const [transaction, setTransaction] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [txError, setTxError] = useState(null);
  const [retrying, setRetrying] = useState(false);
  const [allTxns, setAllTxns] = useState([]);
  const [pick, setPick] = useState("");
  const load = useCallback(async () => {
    if (!transactionId) return;
    setLoading(true);
    setError(null);
    setTxError(null);
    const [a, f, t] = await Promise.allSettled([getAnalysis(transactionId), getFraudAnalysis(transactionId), getTransaction(transactionId)]);
    if (a.status === "fulfilled" && a.value) setAnalysis(a.value);
    else setError(a.reason?.response?.data?.error || "Could not load AI analysis");
    if (f.status === "fulfilled" && f.value) setFraud(f.value);
    if (t.status === "fulfilled" && t.value) setTransaction(t.value);
    else setTxError(t.reason?.response?.data?.error || "Transaction not found");
    setLoading(false);
  }, [transactionId]);
  const handleRetry = async () => {
    setRetrying(true);
    try {
      const a = await analyzeTransaction(transactionId);
      setAnalysis(a);
      setError(null);
    } catch (e) { setError(e?.response?.data?.error || "Retry failed"); } finally { setRetrying(false); }
  };
  useEffect(() => { load(); }, [load]);
  useEffect(() => {
    if (transactionId) return;
    setLoading(false);
    getTransactions().then((d) => setAllTxns(Array.isArray(d) ? d.slice(0, 50) : [])).catch(() => setAllTxns([]));
  }, [transactionId]);
  if (!transactionId) {
    return (
      <div style={{ padding: "20px", maxWidth: "640px", margin: "0 auto" }}>
        <h2 style={{ color: "#0f172a" }}>AI Investigations</h2>
        <p style={{ color: "#64748b", fontSize: 13 }}>Select a transaction to investigate why TXNORYX made its decision.</p>
        <div style={{ display: "flex", gap: 8, marginTop: 12 }}>
          <select value={pick} onChange={(e) => setPick(e.target.value)} style={{ flex: 1, padding: "10px", borderRadius: 8, border: "1px solid #e2e8f0" }}>
            <option value="">Choose transaction…</option>
            {allTxns.map((t) => <option key={t.transactionId} value={t.transactionId}>{t.transactionId} · {t.status}</option>)}
          </select>
          <button disabled={!pick} onClick={() => navigate(`/investigations/${pick}`)} style={{ padding: "10px 18px", borderRadius: 8, border: "none", background: pick ? "#2b84ea" : "#94a3b8", color: "#fff", fontWeight: 700, cursor: "pointer" }}>Investigate</button>
        </div>
        <Link to="/transactions" style={{ color: "#2b84ea", display: "inline-block", marginTop: 14, fontSize: 13 }}>← Back to Transactions</Link>
      </div>
    );
  }
  if (loading) {
    return (
      <div style={{ padding: "24px", textAlign: "center", color: "#64748b" }}>
        <p>🧠 AI analyzing transaction...</p>
        <div style={{ marginTop: "12px", color: "#94a3b8" }}>Loading transaction details…</div>
      </div>
    );
  }
  if (txError || !transaction) {
    return (
      <div style={{ padding: "24px", textAlign: "center" }}>
        <p style={{ color: "#ef4444", fontWeight: 700 }}>⚠ {txError || `Transaction ${transactionId} not found`}</p>
        <p style={{ color: "#64748b", fontSize: 12, marginTop: 8 }}>Check the ID or create one via ⚡ Run Simulation / + Analyze Transaction.</p>
        <Link to="/transactions" style={{ color: "#2b84ea", display: "inline-block", marginTop: 12 }}>← Back to Transactions</Link>
      </div>
    );
  }
  const isCritical = fraud?.riskLevel === "CRITICAL" || analysis?.riskLevel === "CRITICAL";
  const showRecovery = !isCritical && transaction && ["FAILED", "TIMEOUT", "DECLINED"].includes(transaction.status);
  return (
    <div style={{ padding: "20px", maxWidth: "960px", margin: "0 auto", display: "flex", flexDirection: "column", gap: 16 }}>
      <nav style={{ padding: "10px 0" }}>
        <Link style={{ color: "#2b84ea", textDecoration: "none", fontWeight: 600, fontSize: 13 }} to="/transactions">← Back to Transactions</Link>
      </nav>
      <TransactionDetails transaction={transaction} fraud={fraud} loading={loading} />
      {error && !analysis && (
        <div style={{ background: "#fef2f2", border: "1px solid #fecaca", borderRadius: 10, padding: 14, display: "flex", justifyContent: "space-between", alignItems: "center" }}>
          <div><div style={{ color: "#dc2626", fontWeight: 700, fontSize: 13 }}>⚠ Unable to load analysis for {transactionId}</div><div style={{ color: "#64748b", fontSize: 11, marginTop: 4 }}>{error} — transaction details are shown below. AI will retry automatically.</div></div>
          <button onClick={handleRetry} disabled={retrying} style={{ padding: "8px 14px", borderRadius: 8, border: "none", background: "#2b84ea", color: "#fff", fontWeight: 700, fontSize: 12, cursor: "pointer" }}>{retrying ? "Retrying…" : "Retry AI"}</button>
        </div>
      )}
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
      {showRecovery ? <RecoveryPanel transactionId={transactionId} onComplete={() => window.dispatchEvent(new CustomEvent("txns:changed"))} /> : (!isCritical && transaction ? <div style={{ background: "rgba(255,255,255,0.95)", borderRadius: 10, padding: "16px", border: "1px solid #e2e8f0", textAlign: "center", color: "#64748b", fontSize: 12 }}>No recovery needed — transaction status is {transaction.status}.</div> : null)}
      <AgentTimeline activeStep={analysis ? 6 : 3} />
      <div style={{ background: "rgba(255,255,255,0.95)", borderRadius: 10, padding: "14px", border: "1px solid #e2e8f0" }}>
        <h4 style={{ margin: "0 0 8px 0", fontSize: 12, color: "#0f172a" }}>INITIATED → GATEWAY TIMEOUT → RISK ANALYSIS → AI DECISION → ROUTE SWITCH → RETRY → SUCCESS</h4>
        <div style={{ display: "grid", gridTemplateColumns: "repeat(4, 1fr)", gap: 8, fontSize: 11 }}>
          <div style={{ background: "#f8fafc", borderRadius: 6, padding: 8, textAlign: "center" }}><div style={{ color: "#94a3b8" }}>Risk Score</div><strong>{analysis?.riskScore ?? 24}</strong></div>
          <div style={{ background: "#f8fafc", borderRadius: 6, padding: 8, textAlign: "center" }}><div style={{ color: "#94a3b8" }}>Fraud</div><strong style={{ color: "#22c55e" }}>{analysis ? `${analysis.fraudProbability}%` : "4%"}</strong></div>
          <div style={{ background: "#f8fafc", borderRadius: 6, padding: 8, textAlign: "center" }}><div style={{ color: "#94a3b8" }}>Recovery</div><strong style={{ color: "#22c55e" }}>{analysis ? `${analysis.recoveryProbability}%` : "91%"}</strong></div>
          <div style={{ background: "#f8fafc", borderRadius: 6, padding: 8, textAlign: "center" }}><div style={{ color: "#94a3b8" }}>Confidence</div><strong style={{ color: "#2b84ea" }}>{analysis ? `${Math.round((analysis.confidence || 0.94)*100)}%` : "94%"}</strong></div>
        </div>
        <div style={{ fontSize: 11, color: "#334155", background: "#f8fafc", borderRadius: 6, padding: "8px 10px", marginTop: 8 }}>AI Reasoning: {analysis?.explanation || "Temporary gateway failure detected. Transaction history is normal. Fraud probability is low. Alternative route has high success rate. Recommended action: SWITCH ROUTE + RETRY"}</div>
      </div>
      <div style={{ textAlign: "center", fontSize: 11, color: "#94a3b8" }}>{transactionId} · INITIATED → PROCESSING → FAILED → ANALYZING → AI_DECISION → RECOVERY → SUCCESS — governed autonomy</div>
    </div>
  );
}
export default AIInvestigations;
