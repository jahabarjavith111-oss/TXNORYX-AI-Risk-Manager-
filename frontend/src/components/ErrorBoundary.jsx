import React from "react";

export default class ErrorBoundary extends React.Component {
  constructor(props) { super(props); this.state = { hasError: false, error: null }; }
  static getDerivedStateFromError(error) { return { hasError: true, error }; }
  componentDidCatch(error, info) { console.error("ErrorBoundary:", error, info); }
  render() {
    if (this.state.hasError) {
      return (
        <div className="card" style={{ padding: 16, border: "1px solid #fecaca", background: "rgba(239,68,68,0.06)" }}>
          <h3 style={{ color: "#ef4444", margin: "0 0 8px 0" }}>Something went wrong</h3>
          <p style={{ fontSize: 12, color: "#64748b", whiteSpace: "pre-wrap" }}>{String(this.state.error?.message || this.state.error)}</p>
          <button onClick={() => this.setState({ hasError: false, error: null })} style={{ marginTop: 8, padding: "6px 12px", borderRadius: 6, border: "1px solid #e2e8f0", background: "#fff", fontSize: 12, cursor: "pointer" }}>Reset</button>
        </div>
      );
    }
    return this.props.children;
  }
}
