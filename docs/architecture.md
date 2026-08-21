# TXNORYX Architecture

```
                    USER
                      │
                      ▼
              React Dashboard (Vite 5173)
              ├─ Dashboard (KPIs, Pie/Bar, Recent Txns)
              ├─ Transactions (Search/Filter)
              ├─ FraudDetection (Score/Factors)
              ├─ AIInvestigations (AIInsight + RecoveryPanel + AgentTimeline)
              └─ AgentActivity
                      │
                      ▼
              Spring Boot API (8080)
              ├─ TransactionController  → TransactionService → transactions
              ├─ DashboardController    → DashboardService
              ├─ RiskController         → AIAnalysisService → RiskEngine
              ├─ FraudController        → FraudService → FraudDetectionEngine (10 factors)
              ├─ AgentController        → AutonomousWorkflowService → AutonomousAgent + ActionExecutor
              ├─ RecoveryController     → RecoveryService → RecoveryEngine
              └─ HealthController
                      │
        ┌─────────────┼─────────────┐
        ▼             ▼             ▼
   Risk Engine   Fraud Engine   AI Engine (Ollama qwen3.5 / fallback)
        │             │             │
        └─────────────┼─────────────┘
                      ▼
              Autonomous Agent (BLOCK/RETRY/ESCALATE/APPROVE)
                      │
           ┌──────────┼──────────┐
           ▼          ▼          ▼
         RETRY    ESCALATE     BLOCK
           │
           ▼
      Recovery Engine (80% retry, 65% alt route)
           │
           ▼
          MySQL txnoryx (8 tables)
           │
           ▼
         DASHBOARD
```

Tables: users, transactions, transaction_events, ai_analysis, agent_actions, recovery_actions, fraud_analysis, risk_factors
