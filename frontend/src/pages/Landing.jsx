import { Link } from "react-router-dom";
const FEATURES = [
  { t: "Payment Risk Intelligence", d: "Real-time risk scoring on every transaction with transparent reasons, not black-box verdicts." },
  { t: "Recovery Intelligence", d: "Predict recovery probability and pick the retry strategy with the highest expected value." },
  { t: "AI-Powered Decisions", d: "LLM-assisted analysis with deterministic fallbacks, so decisions never depend on AI uptime." },
  { t: "Autonomous Agent", d: "Detect → Understand → Predict → Decide → Govern → Act → Audit, fully autonomous." },
  { t: "Safety & Governance", d: "Configurable fraud, risk, retry and confidence thresholds gate every autonomous action." },
  { t: "Audit Trail", d: "Every decision persisted with inputs, route, confidence and expected value for review." },
];
const STEPS = [
  ["01", "Monitor", "Transactions stream into TXNORYX from your checkout or simulation."],
  ["02", "Analyze", "Risk, fraud and failure engines score every payment in milliseconds."],
  ["03", "Predict", "AI estimates fraud probability, recovery chance and the best route."],
  ["04", "Recover", "The agent retries, switches routes or escalates — governed by safety policy."],
];
export default function Landing() {
  return (
    <div className="lp">
      <header className="lp-nav">
        <div className="lp-brand"><span className="lp-mark">TX</span> TXNORYX</div>
        <nav className="lp-links">
          <a href="#features">Features</a><a href="#how">How it works</a><a href="#platform">Platform</a>
        </nav>
        <div className="lp-cta">
          <Link className="lp-signin" to="/signin">Sign In</Link>
          <Link className="lp-btn" to="/signup">Get Started</Link>
        </div>
      </header>
      <section className="lp-hero">
        <div className="lp-hero-left">
          <div className="lp-pill">Autonomous Payment Risk &amp; Recovery Intelligence</div>
          <h1>Stop losing revenue to failed payments.</h1>
          <p className="lp-sub">TXNORYX detects risk, predicts recovery, and autonomously retries failed payments through the best route — with safety governance on every decision.</p>
          <div className="lp-hero-btns">
            <Link className="lp-btn lp-big" to="/signup">Start Managing Risk</Link>
            <Link className="lp-btn-ghost lp-big" to="/signin">View Demo Dashboard</Link>
          </div>
          <div className="lp-stats">
            <div><strong>86%</strong><span>Recovery rate</span></div>
            <div><strong>96%</strong><span>Best route success</span></div>
            <div><strong>93%</strong><span>AI confidence</span></div>
          </div>
        </div>
        <div className="lp-hero-right">
          <div className="lp-card">
            <div className="lp-card-title">TXN-2026-001 · Live Decision</div>
            <div className="lp-kv"><span>Risk Score</span><strong>62/100 HIGH</strong></div>
            <div className="lp-kv"><span>Fraud Probability</span><strong className="ok">18%</strong></div>
            <div className="lp-kv"><span>Recovery Probability</span><strong className="ok">86%</strong></div>
            <div className="lp-kv"><span>Route</span><strong>ROUTE_B · 96%</strong></div>
            <div className="lp-decision">FINAL DECISION · RETRY</div>
            <div className="lp-safe">Safety Governor · APPROVED</div>
          </div>
        </div>
      </section>
      <section id="features" className="lp-section">
        <h2>Everything you need to recover payments</h2>
        <p className="lp-sec-sub">One platform from detection to audit trail.</p>
        <div className="lp-grid">
          {FEATURES.map((f) => (
            <div key={f.t} className="lp-feat"><h3>{f.t}</h3><p>{f.d}</p></div>
          ))}
        </div>
      </section>
      <section id="how" className="lp-section lp-alt">
        <h2>How TXNORYX works</h2>
        <p className="lp-sec-sub">Monitor → Analyze → Predict → Recover</p>
        <div className="lp-steps">
          {STEPS.map(([n, t, d]) => (
            <div key={n} className="lp-step"><div className="lp-step-n">{n}</div><h3>{t}</h3><p>{d}</p></div>
          ))}
        </div>
      </section>
      <section id="platform" className="lp-section">
        <h2>Inside the platform</h2>
        <p className="lp-sec-sub">Dashboard · Transactions · Fraud Detection · AI Investigations · Agent Activity</p>
        <div className="lp-cta-box">
          <h3>Ready to recover failed payments?</h3>
          <p>Create your TXNORYX account and analyze your first transaction in minutes.</p>
          <Link className="lp-btn lp-big" to="/signup">Create Account</Link>
        </div>
      </section>
      <footer className="lp-foot">TXNORYX · Autonomous Payment Risk &amp; Recovery Intelligence · © 2026</footer>
    </div>
  );
}
