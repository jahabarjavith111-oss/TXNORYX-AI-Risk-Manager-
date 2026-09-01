export default function AIInsight({ analysis, loading, error }) {
  if (loading) {
    return (
      <div style={{ background: "rgba(255,255,255,0.95)", borderRadius: 10, padding: "18px", border: "1px solid #e2e8f0" }}>
        <p style={{ color: "#64748b" }}>🧠 AI analyzing transaction...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div style={{ background: "rgba(255,255,255,0.95)", borderRadius: 10, padding: "18px", border: "1px solid #fecaca" }}>
        <p style={{ color: "#ef4444" }}>⚠ {error}</p>
      </div>
    );
  }

  if (!analysis) {
    return (
      <div style={{ background: "rgba(255,255,255,0.95)", borderRadius: 10, padding: "18px", border: "1px solid #e2e8f0" }}>
        <p style={{ color: "#64748b", fontSize: 12 }}>AI insight will appear after risk analysis.</p>
      </div>
    );
  }

  return (
    <div style={{ background: "rgba(255,255,255,0.95)", borderRadius: 10, padding: "18px", border: "1px solid #e2e8f0" }}>
      <h3 style={{ margin: "0 0 12px 0", color: "#0f172a" }}>🧠 AI Risk Analysis</h3>
      <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr 1fr", gap: 10, marginBottom: 12 }}>
        <Mini label="Risk" value={analysis.riskLevel} color="#f97316" />
        <Mini label="Confidence" value={`${analysis.decisionConfidence ?? ((analysis.confidence ?? 0) * 100).toFixed(0)}%`} color="#22c55e" />
        <Mini label="Score" value={`${analysis.riskScore ?? "—"}/100`} color="#2b84ea" />
      </div>
      <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 10, marginBottom: 12 }}>
        <Bar label="Fraud Probability" value={analysis.fraudProbability ?? 0} color="#ef4444" />
        <Bar label="Recovery Probability" value={analysis.recoveryProbability ?? 0} color="#22c55e" />
      </div>
      <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 10, marginBottom: 12 }}>
        <Bar label="Decision Confidence" value={analysis.decisionConfidence ?? Math.round((analysis.confidence ?? 0)*100)} color="#2b84ea" />
        <Mini label="Risk Level" value={analysis.riskLevel} color={analysis.riskLevel==="CRITICAL"?"#ef4444":analysis.riskLevel==="HIGH"?"#f97316":analysis.riskLevel==="MEDIUM"?"#f59e0b":"#22c55e"} />
      </div>
      {analysis.failureType && <div style={{ fontSize: 12, color: "#334155", background: "#fffbeb", borderRadius: 8, padding: "10px 12px", marginBottom: 8, border: "1px solid #fde68a" }}><strong>Failure:</strong> {analysis.failureType} — {analysis.failureExplanation || "—"}</div>}
      <div style={{ fontSize: 12, color: "#334155", background: "#f8fafc", borderRadius: 8, padding: "10px 12px", marginBottom: 8 }}>
        <strong>Root Cause:</strong> {analysis.rootCause || analysis.explanation || "—"}
      </div>
      <div style={{ fontSize: 12, color: "#334155", background: "rgba(43,132,234,0.06)", borderRadius: 8, padding: "10px 12px" }}>
        <strong>Recommended Action:</strong> <span style={{ color: "#2b84ea", fontWeight: 700 }}>{analysis.recommendation || "—"}</span>
      </div>
      {analysis.explanation && analysis.explanation !== analysis.rootCause && (
        <div style={{ fontSize: 11, color: "#94a3b8", marginTop: 8 }}>{analysis.explanation}</div>
      )}
    </div>
  );
}

function Bar({ label, value, color }) {
  return (
    <div style={{ background: "#f8fafc", borderRadius: 6, padding: "8px" }}>
      <div style={{ display: "flex", justifyContent: "space-between", fontSize: 10, color: "#94a3b8", textTransform: "uppercase", marginBottom: 4 }}><span>{label}</span><span style={{ fontWeight: 700, color }}>{value}%</span></div>
      <div style={{ width: "100%", height: 6, background: "#e2e8f0", borderRadius: 999, overflow: "hidden" }}><div style={{ width: `${Math.min(100, Math.max(0, value))}%`, height: "100%", background: color }} /></div>
    </div>
  );
}

function Mini({ label, value, color }) {
  return (
    <div style={{ textAlign: "center", padding: "8px", background: "#f8fafc", borderRadius: 6 }}>
      <div style={{ fontSize: 10, color: "#94a3b8", textTransform: "uppercase" }}>{label}</div>
      <div style={{ fontWeight: 700, color, fontSize: 13 }}>{value}</div>
    </div>
  );
}
