import { Navigate, Route, Routes } from "react-router-dom";
import Layout from "./components/Layout";
import { ToastProvider } from "./components/Toast";
import Dashboard from "./pages/Dashboard";
import Transactions from "./pages/Transactions";
import AIInvestigations from "./pages/AIInvestigations";
import FraudDetection from "./pages/FraudDetection";
import AgentActivity from "./pages/AgentActivity";
import ErrorBoundary from "./components/ErrorBoundary";
import "./index.css";


function App() {
  return (
    <ToastProvider>
      <Routes>
        <Route element={<Layout />}>
          <Route path="/" element={<Navigate to="/dashboard" replace />} />
          <Route path="/dashboard" element={<ErrorBoundary><Dashboard /></ErrorBoundary>} />
          <Route path="/transactions" element={<Transactions />} />
          <Route path="/investigations/:transactionId" element={<AIInvestigations />} />
          <Route path="/fraud" element={<FraudDetection />} />
          <Route path="/agent" element={<AgentActivity />} />
          <Route path="*" element={<Navigate to="/dashboard" replace />} />
        </Route>
      </Routes>
    </ToastProvider>
  );
}


export default App;
