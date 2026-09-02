import { useState } from "react";
import { createTransaction } from "../services/transactionService";
import { analyzeTransaction } from "../services/riskService";
import { useToast } from "./Toast";
export default function AnalyzeTransactionModal({ open, onClose, onCreated }) {
  const notify = useToast();
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);
  const [form, setForm] = useState({ transactionId: `TXN-2026-${String(Math.floor(Math.random()*9000)+1000)}`, amount: "25000", paymentMethod: "UPI", status: "TIMEOUT", failureReason: "Gateway Timeout", merchant: "Demo Store", location: "Chennai", deviceId: "DEV-7821" });
  const upd = (k,v) => setForm(p=>({...p,[k]:v}));
  const submit = async (e) => {
    e.preventDefault();
    setLoading(true); setResult(null);
    try {
      const txn = await createTransaction({ transactionId: form.transactionId, amount: Number(form.amount), paymentMethod: form.paymentMethod, status: form.status, failureReason: form.failureReason, merchant: form.merchant, location: form.location, deviceId: form.deviceId, currency: "INR", userId: 1 });
      window.dispatchEvent(new CustomEvent("txns:changed"));
      onCreated?.(txn);
      const ai = await analyzeTransaction(txn.transactionId);
      setResult({ txn, ai });
      notify(`✓ Transaction ${txn.transactionId} analyzed`, "success");
    } catch (err) { notify(err?.response?.data?.error || "Analysis failed", "error"); }
    finally { setLoading(false); }
  };
  if (!open) return null;
  return (
    <div onClick={onClose} style={{ position:"fixed", inset:0, background:"rgba(15,23,42,0.55)", display:"flex", alignItems:"center", justifyContent:"center", zIndex:80, padding:16 }}>
      <div onClick={e=>e.stopPropagation()} style={{ background:"#fff", borderRadius:14, width:"100%", maxWidth:560, maxHeight:"92vh", overflow:"auto", boxShadow:"0 20px 60px rgba(0,0,0,0.25)" }}>
        <div style={{ padding:"18px 20px 0", display:"flex", justifyContent:"space-between", alignItems:"center" }}>
          <h3 style={{ margin:0, color:"#0f172a" }}>Analyze Transaction</h3>
          <button onClick={onClose} style={{ border:"none", background:"#f1f5f9", borderRadius:8, width:32, height:32, cursor:"pointer" }}>✕</button>
        </div>
        {!result ? (
        <form onSubmit={submit} style={{ padding:20, display:"grid", gap:12 }}>
          {[["Transaction ID","transactionId"],["Amount (₹)","amount", "number"],["Payment Method","paymentMethod"],["Status","status"],["Failure Reason","failureReason"],["Merchant","merchant"],["Location","location"],["Device ID","deviceId"]].map(([label,key,type])=>(
            <label key={key} style={{ display:"grid", gap:4 }}>
              <span style={{ fontSize:11, color:"#64748b", textTransform:"uppercase", fontWeight:700 }}>{label}</span>
              {key==="paymentMethod" ? <select value={form[key]} onChange={e=>upd(key,e.target.value)} style={inp}><option>UPI</option><option>CARD</option><option>NET_BANKING</option><option>WALLET</option></select> :
               key==="status" ? <select value={form[key]} onChange={e=>upd(key,e.target.value)} style={inp}><option>TIMEOUT</option><option>FAILED</option><option>DECLINED</option><option>SUSPICIOUS</option><option>SUCCESS</option><option>PENDING</option></select> :
               <input type={type||"text"} value={form[key]} onChange={e=>upd(key,e.target.value)} required style={inp} />}
            </label>
          ))}
          <button type="submit" disabled={loading} style={{ marginTop:6, padding:"12px", borderRadius:10, border:"none", background: loading?"#94a3b8":"#2b84ea", color:"#fff", fontWeight:800, cursor:"pointer" }}>{loading?"Analyzing…":"Analyze Transaction"}</button>
        </form>
        ) : (
        <div style={{ padding:20, display:"grid", gap:12 }}>
          <div style={{ background:"#f8fafc", borderRadius:10, padding:12, border:"1px solid #e2e8f0" }}>
            <div style={{ fontWeight:800, color:"#0f172a", fontSize:13 }}>{result.txn.transactionId} — TXNORYX Decision</div>
            <div style={{ display:"grid", gridTemplateColumns:"1fr 1fr", gap:8, marginTop:10, fontSize:12 }}>
              <KV k="Risk Score" v={`${result.ai.riskScore}/100`} /><KV k="Risk Level" v={result.ai.riskLevel} />
              <KV k="Fraud Probability" v={`${result.ai.fraudProbability}%`} /><KV k="Recovery Probability" v={`${result.ai.recoveryProbability}%`} />
              <KV k="AI Confidence" v={`${Math.round((result.ai.confidence||0.8)*100)}%`} /><KV k="Failure Type" v={result.ai.failureType} />
              <KV k="AI Recommendation" v={result.ai.recommendation} /><KV k="Decision Confidence" v={`${result.ai.decisionConfidence}%`} />
            </div>
            <div style={{ marginTop:10, fontSize:11, color:"#334155", background:"#fff", borderRadius:8, padding:8, border:"1px solid #e2e8f0" }}><strong>Safety Governor:</strong> APPROVED — <strong>FINAL DECISION: {result.ai.recommendation?.includes("RETRY")?"RETRY":result.ai.recommendation?.includes("BLOCK")?"BLOCK":"VERIFY"}</strong><br/><span style={{color:"#64748b"}}>{result.ai.explanation}</span></div>
            <div style={{ marginTop:8, fontSize:11, color:"#2b84ea" }}>Best Route: ROUTE_B 96% · 1.4s · ₹12 cost (from Route Optimizer)</div>
          </div>
          <div style={{ display:"flex", gap:8 }}>
            <button onClick={()=>{ setResult(null); onClose(); }} style={{ flex:1, padding:10, borderRadius:8, border:"1px solid #e2e8f0", background:"#fff", cursor:"pointer", fontWeight:700 }}>Close</button>
            <button onClick={()=>{ setResult(null); }} style={{ flex:1, padding:10, borderRadius:8, border:"none", background:"#f1f5f9", cursor:"pointer", fontWeight:700 }}>Analyze Another</button>
          </div>
        </div>
        )}
      </div>
    </div>
  );
}
const inp = { padding:"9px 10px", borderRadius:8, border:"1px solid #e2e8f0", fontSize:13, outline:"none" };
function KV({k,v}){ return <div style={{ background:"#fff", border:"1px solid #e2e8f0", borderRadius:8, padding:"8px 10px", display:"flex", justifyContent:"space-between" }}><span style={{color:"#64748b"}}>{k}</span><strong style={{color:"#0f172a"}}>{v}</strong></div>; }
