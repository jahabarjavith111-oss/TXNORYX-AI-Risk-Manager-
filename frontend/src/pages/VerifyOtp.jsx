import { useEffect, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { resendOtp, verifyOtp } from "../services/authService";
import { useAuth } from "../auth/AuthContext";
import { useToast } from "../components/Toast";
export default function VerifyOtp() {
  const notify = useToast();
  const navigate = useNavigate();
  const loc = useLocation();
  const { setSession } = useAuth();
  const [email, setEmail] = useState(loc.state?.email || "");
  const [otp, setOtp] = useState("");
  const [loading, setLoading] = useState(false);
  const [cool, setCool] = useState(0);
  const [devOtp, setDevOtp] = useState(loc.state?.devOtp || null);
  useEffect(() => {
    if (cool <= 0) return;
    const t = setTimeout(() => setCool((c) => c - 1), 1000);
    return () => clearTimeout(t);
  }, [cool]);
  const submit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const r = await verifyOtp(email, otp);
      setSession(r.token, r.user);
      notify("Account verified - welcome to TXNORYX", "success");
      navigate("/dashboard");
    } catch (err) { notify(err?.response?.data?.error || "Verification failed", "error"); }
    finally { setLoading(false); }
  };
  const resend = async () => {
    if (cool > 0) return;
    try {
      const r = await resendOtp(email);
      setDevOtp(r.devOtp || null);
      setCool(60);
      notify("New OTP sent", "success");
    } catch (err) { notify(err?.response?.data?.error || "Resend failed", "error"); }
  };
  return (
    <div className="auth-wrap auth-single">
      <div className="auth-card">
        <div className="auth-logo-c">TXNORYX</div>
        <h1>Verify your email address</h1>
        <p className="auth-sub">Use the verification code below to complete your account setup.</p>
        {devOtp && <div className="auth-demo">Demo mode — your OTP is <strong>{devOtp}</strong> (valid 10 min)</div>}
        <form onSubmit={submit} className="auth-form">
          <label>Work Email<input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required placeholder="you@company.com" /></label>
          <label>Verification Code<input className="otp-inp" value={otp} onChange={(e) => setOtp(e.target.value.replace(/\D/g, "").slice(0, 6))} required placeholder="••••••" inputMode="numeric" /></label>
          <button className="auth-btn" disabled={loading} type="submit">{loading ? "Verifying…" : "Verify Code"}</button>
        </form>
        <p className="auth-note">Valid for 10 minutes. Never share this code with anyone. If you didn't request this code, you can safely ignore this email.</p>
        <button className="auth-link" onClick={resend} disabled={cool > 0}>{cool > 0 ? `Resend OTP in ${cool}s` : "Resend OTP"}</button>
        <p className="auth-alt"><Link to="/signin">← Back to Sign In</Link></p>
      </div>
    </div>
  );
}
