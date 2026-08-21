const COLORS = { LOW: "#22c55e", MEDIUM: "#f59e0b", HIGH: "#f97316", CRITICAL: "#ef4444" };

export default function RiskCard({ label, value, total }) {
  const color = COLORS[label] || "#94a3b8";
  const pct = total > 0 ? Math.round((value / total) * 100) : 0;
  return (
    <div style={{ background: "rgba(255,255,255,0.95)", borderRadius: 10, padding: "14px", textAlign: "center", borderTop: `4px solid ${color}`, boxShadow: "0 1px 3px rgba(0,0,0,0.06)" }}>
      <div style={{ fontSize: "2rem", fontWeight: 800, color }}>{value}</div>
      <div style={{ fontSize: 11, color: "#64748b", textTransform: "uppercase", letterSpacing: "0.06em", marginBottom: 6 }}>{label}</div>
      <div style={{ width: "100%", height: 6, background: "#e2e8f0", borderRadius: 999, overflow: "hidden" }}>
        <div style={{ width: `${pct}%`, height: "100%", background: color, transition: "width 0.6s ease" }} />
      </div>
      <div style={{ fontSize: 10, color: "#94a3b8", marginTop: 4 }}>{pct}%</div>
    </div>
  );
}
