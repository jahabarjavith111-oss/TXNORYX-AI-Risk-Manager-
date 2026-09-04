import { Navigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
export default function ProtectedRoute({ children }) {
  const { token, ready } = useAuth();
  if (!ready) return <div style={{ padding: 40, textAlign: "center", color: "#64748b" }}>Loading TXNORYX…</div>;
  if (!token) return <Navigate to="/" replace />;
  return children;
}
