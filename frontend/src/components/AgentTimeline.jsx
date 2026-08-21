const STEPS = [
  { label: "Transaction detected", sub: "Ingress" },
  { label: "Risk analyzed", sub: "RiskEngine" },
  { label: "Fraud check completed", sub: "FraudDetection" },
  { label: "AI decision generated", sub: "AI Analysis" },
  { label: "Recovery strategy selected", sub: "RecoveryEngine" },
  { label: "Action executed", sub: "Agent → MySQL" },
];

export default function AgentTimeline({ activeStep = 6 }) {
  return (
    <div style={{ background: "rgba(255,255,255,0.95)", borderRadius: 10, padding: "18px", border: "1px solid #e2e8f0" }}>
      <h3 style={{ margin: "0 0 14px 0", color: "#0f172a" }}>Autonomous Agent</h3>
      <div style={{ display: "flex", flexDirection: "column", gap: 0 }}>
        {STEPS.map((s, i) => {
          const done = i < activeStep;
          return (
            <div key={i} style={{ display: "flex", gap: 12, alignItems: "flex-start" }}>
              <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
                <div
                  style={{
                    width: 22, height: 22, borderRadius: "50%",
                    background: done ? "#22c55e" : "#e2e8f0",
                    color: done ? "#fff" : "#94a3b8",
                    display: "flex", alignItems: "center", justifyContent: "center",
                    fontSize: 10, fontWeight: 700,
                    border: done ? "2px solid #16a34a" : "2px solid #e2e8f0"
                  }}
                >
                  {done ? "✓" : i + 1}
                </div>
                {i < STEPS.length - 1 && (
                  <div style={{ width: 2, height: 18, background: done ? "#bbf7d0" : "#e2e8f0", margin: "4px 0" }} />
                )}
              </div>
              <div style={{ paddingBottom: 14 }}>
                <div style={{ fontWeight: 600, fontSize: 12, color: done ? "#0f172a" : "#94a3b8" }}>{s.label}</div>
                <div style={{ fontSize: 10, color: "#94a3b8" }}>{s.sub}</div>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}
