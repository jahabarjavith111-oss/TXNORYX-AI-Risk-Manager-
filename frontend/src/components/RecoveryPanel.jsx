import { useState } from "react";
import { runRecovery } from "../services/recoveryService";

export default function RecoveryPanel({ transactionId, onComplete }) {
  const [state, setState] = useState("idle");
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);

  const handleRun = async () => {
    setState("running");
    setError(null);
    try {
      const res = await runRecovery(transactionId);
      setResult(res);
      setState(res.status === "RECOVERED" ? "success" : res.status === "ESCALATED" ? "escalated" : "failed");
      onComplete?.(res);
      window.dispatchEvent(new CustomEvent("txns:changed"));
    } catch (e) {
      setError(e.message || "Recovery failed. Transaction has been escalated for manual review.");
      setState("failed");
    }
  };

  return (
    <div style={{ background: "rgba(255,255,255,0.95)", borderRadius: 10, padding: "18px", border: "1px solid #e2e8f0" }}>
      <h3 style={{ margin: "0 0 8px 0", color: "#0f172a" }}>Recovery Intelligence</h3>
      <p style={{ fontSize: 12, color: "#64748b", margin: "0 0 12px 0" }}>
        Autonomous recovery for <strong>{transactionId}</strong>. Strategy selected by Risk + Recovery Engine.
      </p>

      {state === "idle" && (
        <button
          onClick={handleRun}
          style={{ width: "100%", padding: "10px", borderRadius: 8, border: "none", background: "#2b84ea", color: "#fff", fontWeight: 700, cursor: "pointer" }}
        >
          RUN RECOVERY
        </button>
      )}

      {state === "running" && (
        <div style={{ textAlign: "center", padding: "12px", color: "#64748b" }}>
          <div style={{ fontSize: 20, marginBottom: 6 }}>⚙ Executing autonomous recovery...</div>
          <div style={{ fontSize: 12 }}>Analyzing → Selecting Strategy → Executing → Verifying...</div>
        </div>
      )}

      {state === "success" && result && (
        <div style={{ background: "rgba(34,197,94,0.08)", borderRadius: 8, padding: "12px", border: "1px solid rgba(34,197,94,0.25)" }}>
          <div style={{ color: "#16a34a", fontWeight: 800 }}>✓ PAYMENT RECOVERED</div>
          <div style={{ fontSize: 12, color: "#334155", marginTop: 6 }}>
            Strategy: <strong>{result.strategy}</strong> · Attempts: {result.attempts} · Probability: {((result.probability ?? 0) * 100).toFixed(0)}%
          </div>
          <div style={{ fontSize: 11, color: "#64748b", marginTop: 4 }}>{result.message}</div>
        </div>
      )}

      {state === "escalated" && result && (
        <div style={{ background: "rgba(249,115,22,0.08)", borderRadius: 8, padding: "12px", border: "1px solid rgba(249,115,22,0.25)" }}>
          <div style={{ color: "#f97316", fontWeight: 700 }}>Escalated for manual review</div>
          <div style={{ fontSize: 12, color: "#334155", marginTop: 4 }}>{result.message}</div>
        </div>
      )}

      {state === "failed" && (
        <div style={{ background: "rgba(239,68,68,0.06)", borderRadius: 8, padding: "12px", border: "1px solid #fecaca" }}>
          <div style={{ color: "#ef4444", fontWeight: 700 }}>Recovery failed</div>
          <div style={{ fontSize: 12, color: "#64748b", marginTop: 4 }}>{error || result?.message || "Transaction has been escalated for manual review."}</div>
        </div>
      )}

      {(state === "success" || state === "failed" || state === "escalated") && (
        <button
          onClick={() => { setState("idle"); setResult(null); setError(null); }}
          style={{ marginTop: 10, padding: "6px 12px", borderRadius: 6, border: "1px solid #e2e8f0", background: "#fff", fontSize: 12, cursor: "pointer" }}
        >
          Reset
        </button>
      )}
    </div>
  );
}
