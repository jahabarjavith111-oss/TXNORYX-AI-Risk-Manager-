function StatCard({ icon, tone, label, value, sub }) {
  return (
    <div className="card stat-card">
      <div
        className="stat-icon"
        style={{ background: tone.soft, color: tone.main }}
      >
        {icon}
      </div>
      <div>
        <div className="stat-label">{label}</div>
        <div className="stat-value">{value}</div>
        {sub && <div className="stat-sub">{sub}</div>}
      </div>
    </div>
  );
}

export default StatCard;
