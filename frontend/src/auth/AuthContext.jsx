import { createContext, useCallback, useContext, useEffect, useState } from "react";
import api from "../services/api";
import { fetchMe, logoutApi } from "../services/authService";
const AuthContext = createContext(null);
export function useAuth() { return useContext(AuthContext); }
export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try { return JSON.parse(localStorage.getItem("txnoryx_user") || "null"); } catch { return null; }
  });
  const [token, setTokenState] = useState(() => localStorage.getItem("txnoryx_token") || null);
  const [ready, setReady] = useState(false);
  useEffect(() => {
    if (!token) { setReady(true); return; }
    fetchMe().then((d) => {
      setUser(d.user);
      localStorage.setItem("txnoryx_user", JSON.stringify(d.user));
    }).catch(() => {
      localStorage.removeItem("txnoryx_token");
      localStorage.removeItem("txnoryx_user");
      setTokenState(null);
      setUser(null);
    }).finally(() => setReady(true));
  }, []);
  const setSession = useCallback((t, u) => {
    localStorage.setItem("txnoryx_token", t);
    localStorage.setItem("txnoryx_user", JSON.stringify(u));
    setTokenState(t);
    setUser(u);
  }, []);
  const logout = useCallback(async () => {
    await logoutApi();
    localStorage.removeItem("txnoryx_token");
    localStorage.removeItem("txnoryx_user");
    setTokenState(null);
    setUser(null);
  }, []);
  return <AuthContext.Provider value={{ user, token, ready, setSession, logout }}>{children}</AuthContext.Provider>;
}
export function attachAuthInterceptor() {
  api.interceptors.request.use((cfg) => {
    const t = localStorage.getItem("txnoryx_token");
    if (t) cfg.headers.Authorization = `Bearer ${t}`;
    return cfg;
  });
}
