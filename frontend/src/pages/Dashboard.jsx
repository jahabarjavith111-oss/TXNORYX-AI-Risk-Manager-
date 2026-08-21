import { useCallback, useEffect, useState, useMemo } from "react";
import { useNavigate } from "react-router-dom";
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer, BarChart, Bar, XAxis, YAxis } from "recharts";
import StatCard from "../components/StatCard";
import StatusBadge from "../components/StatusBadge";
import RiskCard from "../components/RiskCard";
import { formatCurrency, formatNumber, formatDate, percent } from "../utils/format";
import { getDashboardStats } from "../services/dashboardService";
import { getTransactions } from "../services/transactionService";

const RISK_COLORS_MAP = { LOW: "#22c55e", MEDIUM: "#f59e0b", HIGH: "#f97316", CRITICAL: "#ef4444" };

function Dashboard() {
  const [stats, setStats] = useState(null);
  const [txns, setTxns] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [search, setSearch] = useState("");
  const navigate = useNavigate();

  const load = useCallback(() => {
    setLoading(true);
    Promise.all([getDashboardStats(), getTransactions()])
      .then(([statsData, txnData]) => {
        setStats(statsData);
        setTxns(txnData);
        setError(null);
      })
      .catch(() => {
        setError("Could not load dashboard data");
      })
      .finally(() => setLoading(false));
  }, []);

  useEffect(() => {
    load();
    const onChange = () => load();
    window.addEventListener("txns:changed", onChange);
    return () => window.removeEventListener("txns:changed", onChange);
  }, [load]);

  if (loading && !stats) {
    return (
      <div className="stats-grid">
        {[1,2,3,4].map(i => (
          <div key={i} className="card stat-card">
            <div className="skeleton" style={{ width: 44, height: 44, borderRadius: 12 }} />
            <div style={{ flex: 1 }}>
              <div className="skeleton" style={{ width: 90, height: 10, marginBottom: 10 }} />
              <div className="skeleton" style={{ width: 70, height: 20 }} />
            </div>
          </div>
        ))}
      </div>
    );
  }

  if (error && !stats) {
    return (
      <div className="card">
        <div className="empty-state">
          <h3>Something went wrong</h3>
          <p>{error}</p>
          <p style={{ fontSize: 12, color: "#94a3b8" }}>⚠ Unable to connect to TXNORYX backend. Please check the server.</p>
        </div>
      </div>
    );
  }

  const total = stats?.totalTransactions ?? 0;
  const successful = stats?.successfulTransactions ?? 0;
  const failed = stats?.failedTransactions ?? 0;
  const recovered = stats?.recoveredTransactions ?? 0;
  const riskBreakdown = stats?.riskBreakdown ?? [0, 0, 0, 0];
  const paymentMethodBreakdown = stats?.paymentMethodBreakdown ?? [0, 0, 0, 0];

  const recent = useMemo(() => {
    if (!txns) return [];
    let list = [...txns].filter((t) => t.createdAt).sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
    if (search.trim()) {
      const q = search.toLowerCase();
      list = list.filter(t => String(t.transactionId).toLowerCase().includes(q) || String(t.merchant || "").toLowerCase().includes(q));
    }
    return list.slice(0, 5);
  }, [txns, search]);

  const riskLabels = ["LOW", "MEDIUM", "HIGH", "CRITICAL"];
  const riskData = useMemo(() =>
    riskBreakdown.map((count, i) => ({ name: riskLabels[i], value: count, color: RISK_COLORS_MAP[riskLabels[i]] })),
    [riskBreakdown]
  );
  const methodLabels = ["UPI", "CARD", "NET BANKING", "WALLET"];
  const methodData = useMemo(
    () => paymentMethodBreakdown.map((count, i) => ({ name: methodLabels[i], value: count })),
    [paymentMethodBreakdown]
  );

  const recoveryRate = failed > 0 ? Math.round((recovered / failed) * 100) : 0;
  const criticalTxns = useMemo(() => txns.filter(t => {
    const amt = t.amount ? Number(t.amount) : 0;
    return t.status === "SUSPICIOUS" || amt >= 50000;
  }).slice(0, 3), [txns]);

  return (
    <main style={{ padding: "20px", minHeight: "100vh" }}>
      <div style={{ marginBottom: 20, display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(170px, 1fr))", gap: 16 }}>
        <StatCard icon={<span>📊</span>} tone={{ main: "#667eea", soft: "rgba(102,126,255,0.12)" }} label="Total Transactions" value={formatNumber(total)} sub={`${formatNumber(recovered)} recovered`} />
        <StatCard icon={<span>💰</span>} tone={{ main: "#12b76a", soft: "rgba(18,183,106,0.12)" }} label="Total Volume" value={formatCurrency(stats?.totalVolume)} sub="Across all payments" />
        <StatCard icon={<span>✓</span>} tone={{ main: "#2b84df", soft: "rgba(43,132,234,0.12)" }} label="Successful" value={formatNumber(successful)} sub={`${percent(successful, total)}% success rate`} />
        <StatCard icon={<span>✕</span>} tone={{ main: "#ef4444", soft: "rgba(239,68,68,0.12)" }} label="Failed" value={formatNumber(failed)} sub={`${percent(failed, total)}% failure rate`} />
        <StatCard icon={<span>↻</span>} tone={{ main: "#8b5cf6", soft: "rgba(139,92,246,0.12)" }} label="Recovery Rate" value={`${recoveryRate}%`} sub={`${formatNumber(recovered)} recovered`} />
      </div>

      <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 16, marginBottom: 20 }}>
        <div className="card" style={{ padding: 16 }}>
          <h3 style={{ margin: "0 0 12px 0", color: "#0f172a", fontSize: 14 }}>Risk Distribution</h3>
          <div style={{ display: "grid", gridTemplateColumns: "repeat(4, 1fr)", gap: 8, marginBottom: 12 }}>
            {riskData.map(e => <RiskCard key={e.name} label={e.name} value={e.value} total={total} />)}
          </div>
          <ResponsiveContainer width="100%" height={180}>
            <PieChart>
              <Pie data={riskData} dataKey="value" nameKey="name" cx="50%" cy="50%" outerRadius={70} label={({ name, percent }) => percent > 0 ? `${name} ${(percent * 100).toFixed(0)}%` : ""}>
                {riskData.map((e, i) => <Cell key={i} fill={e.color} />)}
              </Pie>
              <Tooltip />
            </PieChart>
          </ResponsiveContainer>
        </div>

        <div className="card" style={{ padding: 16 }}>
          <h3 style={{ margin: "0 0 12px 0", color: "#0f172a", fontSize: 14 }}>Payment Methods</h3>
          <ResponsiveContainer width="100%" height={180}>
            <BarChart data={methodData}>
              <XAxis dataKey="name" tick={{ fontSize: 10 }} />
              <YAxis tick={{ fontSize: 10 }} />
              <Tooltip />
              <Bar dataKey="value" fill="#667eea" radius={[6, 6, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
          <div style={{ display: "grid", gridTemplateColumns: "repeat(4, 1fr)", gap: 8, marginTop: 8 }}>
            {methodData.map(e => (
              <div key={e.name} style={{ textAlign: "center", padding: 8, background: "#f8fafc", borderRadius: 6 }}>
                <div style={{ fontWeight: 700, color: "#667eea" }}>{e.value}</div>
                <div style={{ fontSize: 10, color: "#64748b" }}>{e.name}</div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {criticalTxns.length > 0 && (
        <div className="card" style={{ padding: 16, marginBottom: 16, border: "1px solid #fecaca", background: "rgba(239,68,68,0.04)" }}>
          <h3 style={{ margin: "0 0 10px 0", color: "#ef4444", fontSize: 13 }}>🚨 Critical Transactions</h3>
          <div style={{ display: "flex", gap: 10, flexWrap: "wrap" }}>
            {criticalTxns.map(t => (
              <div key={t.transactionId} onClick={() => navigate(`/investigations/${t.transactionId}`)} style={{ flex: 1, minWidth: 160, padding: 10, background: "#fff", borderRadius: 8, border: "1px solid #fecaca", cursor: "pointer" }}>
                <div style={{ fontWeight: 700, fontFamily: "monospace", fontSize: 12 }}>{t.transactionId}</div>
                <div style={{ fontSize: 12, color: "#64748b" }}>{formatCurrency(t.amount)} · {t.status}</div>
                <div style={{ fontSize: 11, color: "#ef4444", fontWeight: 600, marginTop: 4 }}>CRITICAL → BLOCK</div>
              </div>
            ))}
          </div>
        </div>
      )}

      <div className="card" style={{ padding: 16 }}>
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 12, gap: 12, flexWrap: "wrap" }}>
          <h3 style={{ margin: 0, color: "#0f172a", fontSize: 14 }}>Recent Transactions</h3>
          <div style={{ display: "flex", gap: 8, alignItems: "center" }}>
            <input
              placeholder="Search transaction..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              style={{ padding: "6px 10px", borderRadius: 6, border: "1px solid #e2e8f0", fontSize: 12, outline: "none" }}
            />
            <button onClick={() => navigate("/transactions")} style={{ padding: "6px 12px", borderRadius: 6, border: "1px solid #e2e8f0", background: "#fff", fontSize: 12, cursor: "pointer" }}>View All</button>
          </div>
        </div>
        {total === 0 ? <p style={{ color: "#64748b" }}>No transactions yet</p> : (
          <div style={{ overflowX: "auto" }}>
            <table style={{ width: "100%", borderCollapse: "collapse" }}>
              <thead>
                <tr style={{ background: "rgba(102,126,255,0.04)", color: "#94a3b8", fontSize: 11 }}>
                  <th style={{ padding: "8px", textAlign: "left" }}>TXN ID</th>
                  <th style={{ padding: "8px", textAlign: "left" }}>Amount</th>
                  <th style={{ padding: "8px", textAlign: "left" }}>Method</th>
                  <th style={{ padding: "8px", textAlign: "left" }}>Status</th>
                  <th style={{ padding: "8px", textAlign: "left" }}>Date</th>
                  <th style={{ padding: "8px", textAlign: "center" }}>Action</th>
                </tr>
              </thead>
              <tbody>
                {recent.map((txn) => (
                  <tr key={txn.transactionId} style={{ borderBottom: "1px solid rgba(0,0,0,0.06)" }}>
                    <td style={{ padding: "8px" }}><strong style={{ fontFamily: "monospace", fontSize: 12 }}>{txn.transactionId}</strong></td>
                    <td style={{ padding: "8px", fontWeight: 600, fontSize: 12 }}>{formatCurrency(txn.amount)}</td>
                    <td style={{ padding: "8px", fontSize: 12 }}>{txn.paymentMethod || "—"}</td>
                    <td style={{ padding: "8px" }}><StatusBadge status={txn.status} /></td>
                    <td style={{ padding: "8px", fontSize: 12, color: "#64748b" }}>{formatDate(txn.createdAt)}</td>
                    <td style={{ padding: "8px", textAlign: "center" }}>
                      <button onClick={() => navigate(`/investigations/${txn.transactionId}`)} style={{ padding: "4px 10px", borderRadius: 6, border: "1px solid #e2e8f0", background: "#fff", color: "#2b84ea", fontWeight: 600, fontSize: 11, cursor: "pointer" }}>View</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      <div style={{ marginTop: 16, display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(120px, 1fr))", gap: 12 }}>
        <div style={{ padding: 12, background: "rgba(255,255,255,0.9)", borderRadius: 8, textAlign: "center" }}>
          <div style={{ fontSize: 11, color: "#94a3b8", textTransform: "uppercase" }}>Agent Decision</div>
          <div style={{ fontSize: 11, color: "#64748b", marginTop: 4 }}>LOW Timeout → RETRY<br/>MEDIUM Declined → VERIFY<br/>HIGH Any → ESCALATE<br/>CRITICAL Any → BLOCK</div>
        </div>
        <div style={{ padding: 12, background: "rgba(255,255,255,0.9)", borderRadius: 8, textAlign: "center" }}>
          <div style={{ fontSize: 11, color: "#94a3b8", textTransform: "uppercase" }}>Recovery</div>
          <div style={{ fontSize: 12, color: "#64748b", marginTop: 4 }}>Strategies: Retry · Alternative Route · Escalate · Block</div>
        </div>
        <div style={{ padding: 12, background: "rgba(255,255,255,0.9)", borderRadius: 8, textAlign: "center" }}>
          <div style={{ fontSize: 11, color: "#94a3b8", textTransform: "uppercase" }}>System</div>
          <div style={{ fontSize: 12, color: "#22c55e", marginTop: 4 }}>● API ONLINE<br/>● DB ONLINE<br/>● AGENT ACTIVE</div>
        </div>
      </div>
    </main>
  );
}

export default Dashboard;
