import api from "./api";


export const getTransactions = async () => {
    const response = await api.get("/transactions");
    return response.data;
};


export const getTransaction = async (transactionId) => {
    const response = await api.get(
        `/transactions/${transactionId}`
    );


    return response.data;
};


export const createTransaction = async (data) => {
    const response = await api.post(
        "/transactions",
        data
    );


    return response.data;
};


export const simulateTransaction = async (scenario) => {
    const response = await api.post(
        "/transactions/simulate",
        { scenario }
    );


    return response.data;
};