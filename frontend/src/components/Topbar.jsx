import { useState } from "react";
import { useLocation } from "react-router-dom";
import { simulateTransaction } from "../services/transactionService";
import { useToast } from "./Toast";

const PAGE_META = {
  "/dashboard": { title: "Dashboard", subtitle: "Payment risk & recovery overview" },
  "/transactions": { title: "Transactions", subtitle: "All payment activity" },
};

function RefreshIcon() {
  return (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M21 12a9 9 0 1 1-2.64-6.36" />
      <path d="M21 3v6h-6" />
    </svg>
  );
}

function BoltIcon() {
  return (
    <svg width="15" height="15" viewBox="0 0 24 24" fill="currentColor">
      <path d="M13 2L4.09 12.97a1 1 0 0 0 .77 1.64H11l-1 7.06 8.91-10.97a1 1 0 0 0-.77-1.64H13l1-7.06z" />
    </svg>
  );
}

function Topbar() {
  const location = useLocation();
  const meta = PAGE_META[location.pathname] || PAGE_META["/dashboard"];
  const notify = useToast();
  const [simulating, setSimulating] = useState(false);

  const handleSimulate = async () => {
    setSimulating(true);
    try {
      const txn = await simulateTransaction("GATEWAY_TIMEOUT");
      notify(`Simulated ${txn.failureReason} — ${txn.transactionId} created`, "success");
      window.dispatchEvent(new CustomEvent("txns:changed"));
    } catch {
      notify("Simulation failed — backend unreachable", "error");
    } finally {
      setSimulating(false);
    }
  };

  return (
    <header className="topbar">
      <div>
        <div className="topbar-title">{meta.title}</div>
        <div className="topbar-subtitle">{meta.subtitle}</div>
      </div>
      <div className="topbar-actions">
        <button
          className="btn btn-secondary btn-icon"
          title="Refresh data"
          onClick={() => window.dispatchEvent(new CustomEvent("txns:changed"))}
        >
          <RefreshIcon />
        </button>
        <button
          className="btn btn-primary"
          onClick={handleSimulate}
          disabled={simulating}
          title="Create a simulated failed transaction"
        >
          {simulating ? <span className="spinner" /> : <BoltIcon />}
          {simulating ? "Simulating…" : "Simulate Failure"}
        </button>
      </div>
    </header>
  );
}

export default Topbar;
