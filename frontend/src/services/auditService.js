import api from "./api";
export const getAudit = (txnId) => api.get(`/audit/${txnId}`).then(r => r.data);
export const getRecentAudit = () => api.get("/audit").then(r => r.data);
