import api from "./api";
export const classifyFailure = (transactionId) => api.get(`/failure/${transactionId}`).then(r => r.data);
export const analyzeFailure = (reason, status) => api.post("/failure/analyze", { reason, status }).then(r => r.data);
