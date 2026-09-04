import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { signin } from "../services/authService";
import { useAuth } from "../auth/AuthContext";
import { useToast } from "../components/Toast";
export default function Signin() {
  const notify = useToast();
  const navigate = useNavigate();
  const { setSession } = useAuth();
  const [loading, setLoading] = useState(false);
  const [form, setForm] = useState({ email: "", password: "" });
  const submit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const r = await signin(form.email, form.password);
      setSession(r.token, r.user);
      notify("Welcome back", "success");
      navigate("/dashboard");
    } catch (err) {
      const msg = err?.response?.data?.error || "Sign in failed";
      notify(msg, "error");
      if (msg.includes("not verified")) navigate("/verify-otp", { state: { email: form.email } });
    }
    finally { setLoading(false); }
  };
  return (
    <div className="auth-wrap auth-single">
      <div className="auth-card">
        <Link to="/" className="auth-logo-c">TXNORYX</Link>
        <h1>Sign in to TXNORYX</h1>
        <p className="auth-sub">Access your payment risk &amp; recovery dashboard.</p>
        <form onSubmit={submit} className="auth-form">
          <label>Email<input type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} required placeholder="you@company.com" /></label>
          <label>Password<input type="password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} required placeholder="Your password" /></label>
          <button className="auth-btn" disabled={loading} type="submit">{loading ? "Signing in…" : "Sign In"}</button>
        </form>
        <p className="auth-alt">New to TXNORYX? <Link to="/signup">Create account</Link></p>
      </div>
    </div>
  );
}
