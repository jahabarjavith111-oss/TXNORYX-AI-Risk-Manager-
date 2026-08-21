import { formatCurrency, formatDate } from "../utils/format";
import StatusBadge from "./StatusBadge";

const RISK_COLORS = { LOW: "#22c55e", MEDIUM: "#f59e0b", HIGH: "#f97316", CRITICAL: "#ef4444" };

export default function TransactionDetails({ transaction, fraud, loading }) {
  if (!transaction) {
    return <p style={{ color: "#64748b" }}>Select a transaction to view details.</p>;
  }

  return (
    <div style={{ background: "rgba(255,255,255,0.95)", borderRadius: 10, padding: "18px", border: "1px solid #e2e8f0" }}>
      <h3 style={{ margin: "0 0 14px 0", color: "#0f172a" }}>Transaction Details</h3>
      <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "10px", fontSize: 13 }}>
        <Field label="Transaction ID" value={transaction.transactionId} mono />
        <Field label="Amount" value={formatCurrency(transaction.amount)} bold />
        <Field label="Status" value={<StatusBadge status={transaction.status} />} />
        <Field label="Method" value={transaction.paymentMethod} />
        <Field label="Merchant" value={transaction.merchant} />
        <Field label="Date" value={formatDate(transaction.createdAt)} />
        <Field label="Device" value={transaction.deviceId || "—"} />
        <Field label="Location" value={transaction.location || "—"} />
      </div>

      {loading && <p style={{ color: "#64748b", marginTop: 14 }}>Analyzing fraud...</p>}

      {fraud && (
        <div style={{ marginTop: 16, paddingTop: 16, borderTop: "1px solid #e2e8f0" }}>
          <h4 style={{ margin: "0 0 10px 0", color: "#0f172a" }}>Fraud Intelligence</h4>
          <div style={{ display: "flex", gap: 12, flexWrap: "wrap", marginBottom: 12 }}>
            <span style={{ padding: "6px 12px", borderRadius: 6, background: RISK_COLORS[fraud.riskLevel] || "#94a3b8", color: "#fff", fontWeight: 700, fontSize: 12 }}>
              {fraud.riskLevel} — {fraud.riskScore}/100
            </span>
            <span style={{ padding: "6px 12px", borderRadius: 6, background: fraud.suspicious ? "rgba(239,68,68,0.12)" : "rgba(34,197,94,0.12)", color: fraud.suspicious ? "#ef4444" : "#22c55e", fontWeight: 700, fontSize: 12 }}>
              {fraud.suspicious ? "SUSPICIOUS" : "CLEAN"}
            </span>
            <span style={{ padding: "6px 12px", borderRadius: 6, background: "rgba(43,132,234,0.10)", color: "#2b84ea", fontWeight: 600, fontSize: 12 }}>
              {fraud.recommendation}
            </span>
          </div>
          {fraud.factors && fraud.factors.length > 0 ? (
            <div style={{ display: "flex", flexDirection: "column", gap: 6 }}>
              {fraud.factors.map((f, i) => (
                <div key={i} style={{ display: "flex", alignItems: "center", gap: 10, fontSize: 12, padding: "8px 10px", background: "#f8fafc", borderRadius: 6 }}>
                  <span style={{ minWidth: 36, textAlign: "center", fontWeight: 700, color: "#fff", background: f.score >= 25 ? "#ef4444" : f.score >= 15 ? "#f97316" : "#22c55e", borderRadius: 4, padding: "3px 6px" }}>+{f.score}</span>
                  <div>
                    <div style={{ fontWeight: 600 }}>{f.factor}</div>
                    <div style={{ color: "#64748b" }}>{f.explanation}</div>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <p style={{ color: "#22c55e", fontSize: 12, fontWeight: 600 }}>No risk factors detected.</p>
          )}
        </div>
      )}
    </div>
  );
}

function Field({ label, value, mono, bold }) {
  return (
    <div>
      <div style={{ fontSize: 10, color: "#94a3b8", textTransform: "uppercase", letterSpacing: "0.06em", marginBottom: 2 }}>{label}</div>
      <div style={{ fontWeight: bold ? 700 : mono ? 600 : 500, fontFamily: mono ? "monospace" : "inherit", fontSize: mono ? 12 : 13 }}>{value}</div>
    </div>
  );
}
