import api from "./api";
export const signup = async (data) => {
  const r = await api.post("/auth/signup", data);
  return r.data;
};
export const verifyOtp = async (email, otp) => {
  const r = await api.post("/auth/verify-otp", { email, otp });
  return r.data;
};
export const signin = async (email, password) => {
  const r = await api.post("/auth/signin", { email, password });
  return r.data;
};
export const resendOtp = async (email) => {
  const r = await api.post("/auth/resend-otp", { email });
  return r.data;
};
export const fetchMe = async () => {
  const r = await api.get("/auth/me");
  return r.data;
};
export const logoutApi = async () => {
  try { await api.post("/auth/logout"); } catch { /* ignore */ }
};
