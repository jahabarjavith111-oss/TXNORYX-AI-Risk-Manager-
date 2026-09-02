import api, { aiApi } from "./api";
export const analyzeTransaction = async (transactionId) => {
    const response = await aiApi.post(`/risk/analyze/${transactionId}`);
    return response.data;
};
export const getAnalysis = async (transactionId) => {
    try {
        const response = await aiApi.get(`/risk/analysis/${transactionId}`);
        return response.data;
    } catch (e) {
        const status = e?.response?.status;
        if (status === 404 || status === 500 || e.code === "ECONNABORTED") {
            const r2 = await aiApi.post(`/risk/analyze/${transactionId}`);
            return r2.data;
        }
        throw e;
    }
};