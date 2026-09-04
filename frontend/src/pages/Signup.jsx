import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { signup } from "../services/authService";
import { useToast } from "../components/Toast";
export default function Signup() {
  const notify = useToast();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [form, setForm] = useState({ name: "", email: "", organization: "", password: "", confirmPassword: "", agree: false });
  const upd = (k, v) => setForm((p) => ({ ...p, [k]: v }));
  const submit = async (e) => {
    e.preventDefault();
    if (!form.agree) { notify("Please accept the Terms & Privacy Policy", "error"); return; }
    if (form.password !== form.confirmPassword) { notify("Passwords do not match", "error"); return; }
    setLoading(true);
    try {
      const r = await signup({ name: form.name, email: form.email, organization: form.organization, password: form.password, confirmPassword: form.confirmPassword });
      notify("Account created - check your email for the OTP", "success");
      navigate("/verify-otp", { state: { email: r.email } });
    } catch (err) { notify(err?.response?.data?.error || "Signup failed", "error"); }
    finally { setLoading(false); }
  };
  return (
    <div className="auth-wrap">
      <div className="auth-left">
        <Link to="/" className="auth-logo"><span className="lp-mark">TX</span> TXNORYX</Link>
        <h1>Create your TXNORYX account</h1>
        <p className="auth-sub">Start managing payment risk and recovery intelligence.</p>
        <form onSubmit={submit} className="auth-form">
          <label>Full Name<input value={form.name} onChange={(e) => upd("name", e.target.value)} required maxLength={50} placeholder="Abdul Rahman" /></label>
          <label>Work Email<input type="email" value={form.email} onChange={(e) => upd("email", e.target.value)} required placeholder="you@company.com" /></label>
          <label>Organization / Merchant Name<input value={form.organization} onChange={(e) => upd("organization", e.target.value)} required maxLength={100} placeholder="Demo Store" /></label>
          <label>Password<input type="password" value={form.password} onChange={(e) => upd("password", e.target.value)} required minLength={8} placeholder="Min 8 chars, number + symbol" /></label>
          <label>Confirm Password<input type="password" value={form.confirmPassword} onChange={(e) => upd("confirmPassword", e.target.value)} required placeholder="Repeat password" /></label>
          <label className="auth-check"><input type="checkbox" checked={form.agree} onChange={(e) => upd("agree", e.target.checked)} /> I agree to the Terms &amp; Privacy Policy</label>
          <button className="auth-btn" disabled={loading} type="submit">{loading ? "Creating…" : "Create Account"}</button>
        </form>
        <p className="auth-alt">Already have an account? <Link to="/signin">Sign In</Link></p>
      </div>
      <div className="auth-right">
        <h2>Payment Recovery Intelligence</h2>
        <p className="auth-flow">Monitor → Analyze → Predict → Recover</p>
        <div className="auth-feats">
          <div><span>AI Risk Detection</span><strong className="ok">✓ Active</strong></div>
          <div><span>Recovery Intelligence</span><strong className="ok">✓ Active</strong></div>
          <div><span>Autonomous Decisions</span><strong className="ok">✓ Active</strong></div>
          <div><span>Safety Governance</span><strong className="ok">✓ Enabled</strong></div>
        </div>
        <p className="auth-brand">TXNORYX<br />Autonomous Payment Risk &amp; Recovery Intelligence</p>
      </div>
    </div>
  );
}
