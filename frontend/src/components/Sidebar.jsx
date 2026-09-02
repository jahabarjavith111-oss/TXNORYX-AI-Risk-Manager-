import { useEffect, useState } from "react";
import { NavLink } from "react-router-dom";
import api from "../services/api";

function LogoMark() {
  return (
    <div className="brand-mark">
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
        <path
          d="M12 2L4 5.5v5.1c0 5.05 3.4 9.76 8 10.9 4.6-2.14 8-6.85 8-10.9V5.5L12 2z"
          fill="#fff"
          fillOpacity="0.95"
        />
        <path
          d="M8.7 12.1l2.3 2.3 4.3-4.6"
          stroke="#2b84ea"
          strokeWidth="2.1"
          strokeLinecap="round"
          strokeLinejoin="round"
        />
      </svg>
    </div>
  );
}

function GridIcon() {
  return (
    <svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <rect x="3" y="3" width="7" height="7" rx="1.5" />
      <rect x="14" y="3" width="7" height="7" rx="1.5" />
      <rect x="14" y="14" width="7" height="7" rx="1.5" />
      <rect x="3" y="14" width="7" height="7" rx="1.5" />
    </svg>
  );
}

function ListIcon() {
  return (
    <svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <line x1="8" y1="6" x2="21" y2="6" />
      <line x1="8" y1="12" x2="21" y2="12" />
      <line x1="8" y1="18" x2="21" y2="18" />
      <line x1="3" y1="6" x2="3.01" y2="6" />
      <line x1="3" y1="12" x2="3.01" y2="12" />
      <line x1="3" y1="18" x2="3.01" y2="18" />
    </svg>
  );
}

function ShieldIcon() {
  return (
    <svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z" />
    </svg>
  );
}

function BotIcon() {
  return (
    <svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <rect x="4" y="8" width="16" height="12" rx="2" />
      <circle cx="9" cy="14" r="1.2" fill="currentColor" stroke="none" />
      <circle cx="15" cy="14" r="1.2" fill="currentColor" stroke="none" />
      <line x1="12" y1="8" x2="12" y2="4" />
      <circle cx="12" cy="3" r="1" fill="currentColor" stroke="none" />
    </svg>
  );
}

function AiIcon() {
  return (
    <svg width="17" height="17" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M21 16V8a2 2 0 0 0-2-2H5a2 2 0 0 0-2 2v8" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
      <polyline points="17 8 12 1 7 8" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
    </svg>
  );
}

const REFRESH_MS = 30000;

function Sidebar() {
  const [healthUp, setHealthUp] = useState(null);

  useEffect(() => {
    let active = true;

    const check = () =>
      api
        .get("/health", { timeout: 4000 })
        .then(() => active && setHealthUp(true))
        .catch(() => active && setHealthUp(false));

    check();
    const interval = setInterval(check, REFRESH_MS);
    return () => {
      active = false;
      clearInterval(interval);
    };
  }, []);

  return (
    <aside className="sidebar">
      <div className="sidebar-brand">
        <LogoMark />
        <div>
          <div className="brand-name">TXNORYX</div>
          <div className="brand-tag">Payment Intelligence</div>
        </div>
      </div>

      <div className="sidebar-section-label">Menu</div>
      <nav className="sidebar-nav">
        <NavLink to="/dashboard" className={({ isActive }) => `sidebar-link${isActive ? " active" : ""}`}>
          <GridIcon />
          <span className="link-label">Dashboard</span>
        </NavLink>
        <NavLink to="/transactions" className={({ isActive }) => `sidebar-link${isActive ? " active" : ""}`}>
          <ListIcon />
          <span className="link-label">Transactions</span>
        </NavLink>
        <NavLink to="/fraud" className={({ isActive }) => `sidebar-link${isActive ? " active" : ""}`}>
          <ShieldIcon />
          <span className="link-label">Fraud Detection</span>
        </NavLink>
        <NavLink to="/agent" className={({ isActive }) => `sidebar-link${isActive ? " active" : ""}`}>
          <BotIcon />
          <span className="link-label">Agent Activity</span>
        </NavLink>
        <NavLink to="/investigations" className={({ isActive }) => `sidebar-link${isActive ? " active" : ""}`}>
          <AiIcon />
          <span className="link-label">AI Investigations</span>
        </NavLink>
      </nav>

      <div className="sidebar-footer">
        <div className={`health-pill ${healthUp ? "up" : healthUp === false ? "down" : ""}`}>
          <span className="health-dot" />
          <span className="health-text">
            {healthUp === null ? "Checking…" : healthUp ? "Backend Operational" : "Backend Offline"}
          </span>
        </div>
      </div>
    </aside>
  );
}

export default Sidebar;
