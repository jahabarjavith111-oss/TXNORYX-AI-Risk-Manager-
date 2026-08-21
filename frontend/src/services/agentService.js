import api from "./api";

export const executeAgent = async (transactionId) => {
    const response = await api.post(`/agent/execute/${transactionId}`);
    return response.data;
};

export const getAgentActivity = async () => {
    const response = await api.get("/agent/activity");
    return response.data;
};
