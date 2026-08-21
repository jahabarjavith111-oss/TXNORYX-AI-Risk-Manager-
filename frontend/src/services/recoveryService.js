import api from "./api";

export const runRecovery = async (transactionId) => {
  const response = await api.post(`/recovery/${transactionId}`);
  return response.data;
};

export const getRecoveryStatus = async (transactionId) => {
  const response = await api.get(`/recovery/${transactionId}`);
  return response.data;
};
