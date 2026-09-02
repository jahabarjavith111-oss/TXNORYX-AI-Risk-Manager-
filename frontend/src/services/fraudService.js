import api, { aiApi } from "./api";
export const analyzeFraud = async (transactionId) => {
    const response = await aiApi.post(`/fraud/analyze/${transactionId}`);
    return response.data;
};
export const getFraudAnalysis = async (transactionId) => {
    try {
        const response = await aiApi.get(`/fraud/analysis/${transactionId}`);
        return response.data;
    } catch (e) {
        const status = e?.response?.status;
        if (status === 404 || status === 500 || e.code === "ECONNABORTED") {
            try {
                const r2 = await aiApi.post(`/fraud/analyze/${transactionId}`);
                return r2.data;
            } catch {}
        }
        throw e;
    }
};
