import { Navigate, Route, Routes } from "react-router-dom";
import Layout from "./components/Layout";
import { ToastProvider } from "./components/Toast";
import { AuthProvider, attachAuthInterceptor } from "./auth/AuthContext";
import ProtectedRoute from "./components/ProtectedRoute";
import Dashboard from "./pages/Dashboard";
import Transactions from "./pages/Transactions";
import AIInvestigations from "./pages/AIInvestigations";
import FraudDetection from "./pages/FraudDetection";
import AgentActivity from "./pages/AgentActivity";
import Landing from "./pages/Landing";
import Signup from "./pages/Signup";
import VerifyOtp from "./pages/VerifyOtp";
import Signin from "./pages/Signin";
import ErrorBoundary from "./components/ErrorBoundary";
import "./index.css";

attachAuthInterceptor();

function App() {
  return (
    <ToastProvider>
      <AuthProvider>
        <Routes>
          <Route path="/" element={<Landing />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/verify-otp" element={<VerifyOtp />} />
          <Route path="/signin" element={<Signin />} />
          <Route element={<ProtectedRoute><Layout /></ProtectedRoute>}>
            <Route path="/dashboard" element={<ErrorBoundary><Dashboard /></ErrorBoundary>} />
            <Route path="/transactions" element={<Transactions />} />
            <Route path="/investigations/:transactionId" element={<AIInvestigations />} />
            <Route path="/investigations" element={<AIInvestigations />} />
            <Route path="/fraud" element={<FraudDetection />} />
            <Route path="/agent" element={<AgentActivity />} />
          </Route>
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </AuthProvider>
    </ToastProvider>
  );
}

export default App;
