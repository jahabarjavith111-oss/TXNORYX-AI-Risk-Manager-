import { useEffect, useState } from "react";
import { getAgentActivity } from "../services/agentService";

const ACTION_COLORS = {
  APPROVE: "#22c55e",
  RETRY_PAYMENT: "#3b82f6",
  VERIFY_PAYMENT: "#8b5cf6",
  ESCALATE: "#f97316",
  BLOCK: "#ef4444",
};

function formatTime(ts) {
  if (!ts) return "—";
  const d = new Date(ts);
  return d.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit", second: "2-digit" });
}

function AgentActivity() {
  const [activities, setActivities] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    let active = true;
    setLoading(true);
    getAgentActivity()
      .then((data) => {
        if (active) setActivities(Array.isArray(data) ? data : []);
      })
      .catch(() => active && setError("Could not load agent activity"))
      .finally(() => active && setLoading(false));
    return () => {
      active = false;
    };
  }, []);

  if (loading) {
    return (
      <div style={{ padding: "24px", textAlign: "center", color: "#64748b" }}>
        Loading agent activity…
      </div>
    );
  }

  if (error) {
    return (
      <div style={{ padding: "24px", textAlign: "center", color: "#ef4444" }}>
        {error}
      </div>
    );
  }

  return (
    <div style={{ padding: "24px", maxWidth: "900px", margin: "0 auto" }}>
      <h2 style={{ marginBottom: "6px" }}>Autonomous Agent Activity</h2>
      <p style={{ color: "#64748b", marginBottom: "20px" }}>
        Every autonomous decision executed by the TXNORYX agent, persisted to database.
      </p>

      {activities.length === 0 ? (
        <div
          style={{
            background: "rgba(255,255,255,0.85)",
            borderRadius: 10,
            padding: "32px",
            textAlign: "center",
            color: "#64748b",
          }}
        >
          No agent activity yet. Execute an agent action from a transaction to see it here.
        </div>
      ) : (
        <div
          style={{
            background: "rgba(255,255,255,0.9)",
            borderRadius: 10,
            padding: "18px",
            overflowX: "auto",
            boxShadow: "0 1px 3px rgba(0,0,0,0.06)",
          }}
        >
          <table style={{ width: "100%", borderCollapse: "collapse" }}>
            <thead>
              <tr style={{ borderBottom: "2px solid rgba(102,126,255,0.25)", color: "#64748b", fontSize: 12 }}>
                <th style={{ padding: "10px 8px", textAlign: "left" }}>TRANSACTION</th>
                <th style={{ padding: "10px 8px", textAlign: "left" }}>ACTION</th>
                <th style={{ padding: "10px 8px", textAlign: "left" }}>STATUS</th>
                <th style={{ padding: "10px 8px", textAlign: "left" }}>MESSAGE</th>
                <th style={{ padding: "10px 8px", textAlign: "right" }}>CONFIDENCE</th>
                <th style={{ padding: "10px 8px", textAlign: "right" }}>TIME</th>
              </tr>
            </thead>
            <tbody>
              {activities.map((a) => (
                <tr key={a.id} style={{ borderBottom: "1px solid rgba(0,0,0,0.06)" }}>
                  <td style={{ padding: "10px 8px", fontWeight: 600, color: "#334155" }}>{a.transactionId}</td>
                  <td style={{ padding: "10px 8px" }}>
                    <span
                      style={{
                        fontSize: 11,
                        fontWeight: 700,
                        color: "#fff",
                        background: ACTION_COLORS[a.action] || "#94a3b8",
                        borderRadius: 5,
                        padding: "4px 8px",
                      }}
                    >
                      {a.action}
                    </span>
                  </td>
                  <td style={{ padding: "10px 8px", color: a.status === "SUCCESS" || a.status === "APPROVED" ? "#22c55e" : "#64748b", fontWeight: 600 }}>
                    {a.status}
                  </td>
                  <td style={{ padding: "10px 8px", color: "#64748b", fontSize: 13 }}>{a.message}</td>
                  <td style={{ padding: "10px 8px", textAlign: "right", color: "#667eea", fontWeight: 600 }}>
                    {(a.confidence ?? 0).toFixed(2)}
                  </td>
                  <td style={{ padding: "10px 8px", textAlign: "right", color: "#94a3b8", fontSize: 12 }}>
                    {formatTime(a.createdAt)}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default AgentActivity;
