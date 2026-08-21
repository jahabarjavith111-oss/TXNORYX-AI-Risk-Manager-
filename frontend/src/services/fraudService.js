import api from "./api";

export const analyzeFraud = async (transactionId) => {
    const response = await api.post(`/fraud/analyze/${transactionId}`);
    return response.data;
};

export const getFraudAnalysis = async (transactionId) => {
    const response = await api.get(`/fraud/analysis/${transactionId}`);
    return response.data;
};
