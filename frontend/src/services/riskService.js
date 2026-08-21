import api from "./api";

export const analyzeTransaction = async (transactionId) => {
    const response = await api.post(`/risk/analyze/${transactionId}`);
    return response.data;
};

export const getAnalysis = async (transactionId) => {
    const response = await api.get(`/risk/analysis/${transactionId}`);
    return response.data;
};