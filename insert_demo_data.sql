USE txnoryx;

-- Insert users first
INSERT INTO users (id, name, email, created_at) VALUES
(1, 'John Doe', 'john@example.com', NOW()),
(2, 'Jane Smith', 'jane@example.com', NOW()),
(3, 'Bob Wilson', 'bob@example.com', NOW());

-- Insert transactions with various statuses and payment methods
-- SUCCESS transactions
INSERT INTO transactions (transaction_id, amount, currency, payment_method, status, merchant, failure_reason, device_id, location, user_id, created_at) VALUES
('txn-001', 150.00, 'USD', 'UPI', 'SUCCESS', 'Amazon', NULL, 'dev-001', 'NY', 1, NOW()),
('txn-002', 299.50, 'USD', 'CARD', 'SUCCESS', 'Flipkart', NULL, 'dev-002', 'CA', 2, NOW()),
('txn-003', 75.00, 'USD', 'NET BANKING', 'SUCCESS', 'Myntra', NULL, 'dev-003', 'TX', 3, NOW()),
('txn-004', 500.00, 'USD', 'WALLET', 'SUCCESS', 'Snapdeal', NULL, 'dev-004', 'NY', 1, NOW()),
('txn-005', 350.00, 'USD', 'UPI', 'SUCCESS', 'Paytm', NULL, 'dev-005', 'CA', 2, NOW()),

-- FAILED transactions
('txn-006', 100.00, 'USD', 'CARD', 'FAILED', 'Uber', 'Invalid card number', 'dev-001', 'NY', 1, NOW()),
('txn-007', 200.00, 'USD', 'NET BANKING', 'FAILED', 'Swiggy', 'Insufficient funds', 'dev-002', 'CA', 2, NOW()),
('txn-008', 75.00, 'USD', 'UPI', 'FAILED', 'MakeMyTrip', 'UPI PIN expired', 'dev-003', 'TX', 3, NOW()),

-- TIMEOUT transactions
('txn-009', 300.00, 'USD', 'CARD', 'TIMEOUT', 'AirAsia', 'Payment gateway timeout', 'dev-004', 'NY', 1, NOW()),
('txn-010', 150.00, 'USD', 'WALLET', 'TIMEOUT', 'Netflix', 'Bank response timeout', 'dev-005', 'CA', 2, NOW()),

-- DECLINED transactions
('txn-011', 50.00, 'USD', 'UPI', 'DECLINED', 'Paytm', 'Transaction declined by bank', 'dev-001', 'TX', 3, NOW()),
('txn-012', 250.00, 'USD', 'CARD', 'DECLINED', 'Amazon', 'Card declined', 'dev-002', 'NY', 1, NOW()),

-- SUSPICIOUS transactions (high-risk)
('txn-013', 5000.00, 'USD', 'CARD', 'SUSPICIOUS', 'Amazon', 'Large value transaction', 'dev-003', 'CA', 2, NOW()),
('txn-014', 2000.00, 'USD', 'NET BANKING', 'SUSPICIOUS', 'Flipkart', 'High-risk merchant', 'dev-004', 'TX', 3, NOW()),
('txn-015', 1000.00, 'USD', 'UPI', 'SUSPICIOUS', 'Myntra', 'Unusual pattern detected', 'dev-005', 'NY', 1, NOW()),

-- RECOVERED transactions
('txn-016', 300.00, 'USD', 'WALLET', 'RECOVERED', 'Snapdeal', 'Recovered after retry', 'dev-001', 'CA', 2, NOW()),
('txn-017', 120.00, 'USD', 'CARD', 'RECOVERED', 'Uber', 'Recovered after timeout', 'dev-002', 'TX', 3, NOW());

-- More transactions to reach 50+
INSERT INTO transactions (transaction_id, amount, currency, payment_method, status, merchant, failure_reason, device_id, location, user_id, created_at) VALUES
('txn-018', 85.50, 'USD', 'UPI', 'SUCCESS', 'MakeMyTrip', NULL, 'dev-001', 'NY', 1, NOW()),
('txn-019', 450.00, 'USD', 'CARD', 'FAILED', 'Paytm', 'Card blocked', 'dev-002', 'CA', 2, NOW()),
('txn-020', 220.00, 'USD', 'NET BANKING', 'TIMEOUT', 'Amazon', 'Gateway timeout', 'dev-003', 'TX', 3, NOW()),
('txn-021', 600.00, 'USD', 'WALLET', 'SUCCESS', 'Flipkart', NULL, 'dev-004', 'NY', 1, NOW()),
('txn-022', 180.00, 'USD', 'UPI', 'DECLINED', 'Swiggy', 'Insufficient UPI balance', 'dev-005', 'CA', 2, NOW()),
('txn-023', 350.00, 'USD', 'CARD', 'SUSPICIOUS', 'Myntra', 'Suspicious activity', 'dev-001', 'TX', 3, NOW()),
('txn-024', 550.00, 'USD', 'NET BANKING', 'RECOVERED', 'Amazon', 'Recovered after review', 'dev-002', 'NY', 1, NOW()),
('txn-025', 95.00, 'USD', 'UPI', 'SUCCESS', 'Uber', NULL, 'dev-003', 'CA', 2, NOW()),

('txn-026', 700.00, 'USD', 'CARD', 'FAILED', 'Flipkart', 'Risk flagged', 'dev-004', 'TX', 3, NOW()),
('txn-027', 300.00, 'USD', 'WALLET', 'SUCCESS', 'Paytm', NULL, 'dev-005', 'NY', 1, NOW()),
('txn-028', 250.00, 'USD', 'UPI', 'TIMEOUT', 'MakeMyTrip', 'Timeout', 'dev-001', 'CA', 2, NOW()),
('txn-029', 400.00, 'USD', 'CARD', 'DECLINED', 'Amazon', 'Declined', 'dev-002', 'TX', 3, NOW()),
('txn-030', 150.00, 'USD', 'NET BANKING', 'SUCCESS', 'Swiggy', NULL, 'dev-003', 'NY', 1, NOW()),

('txn-031', 500.00, 'USD', 'UPI', 'SUSPICIOUS', 'Flipkart', 'High risk', 'dev-004', 'CA', 2, NOW()),
('txn-032', 350.00, 'USD', 'WALLET', 'RECOVERED', 'Myntra', 'Recovered', 'dev-005', 'TX', 3, NOW()),
('txn-033', 200.00, 'USD', 'CARD', 'FAILED', 'Amazon', 'Invalid card', 'dev-001', 'NY', 1, NOW()),
('txn-034', 600.00, 'USD', 'NET BANKING', 'SUCCESS', 'Paytm', NULL, 'dev-002', 'CA', 2, NOW()),
('txn-035', 180.00, 'USD', 'UPI', 'DECLINED', 'Swiggy', 'Declined', 'dev-003', 'TX', 3, NOW()),

('txn-036', 800.00, 'USD', 'CARD', 'SUCCESS', 'Flipkart', NULL, 'dev-004', 'NY', 1, NOW()),
('txn-037', 300.00, 'USD', 'WALLET', 'FAILED', 'Amazon', 'Insufficient funds', 'dev-005', 'CA', 2, NOW()),
('txn-038', 220.00, 'USD', 'UPI', 'TIMEOUT', 'Myntra', 'Gateway timeout', 'dev-001', 'TX', 3, NOW()),
('txn-039', 450.00, 'USD', 'NET BANKING', 'DECLINED', 'Paytm', 'Declined', 'dev-002', 'NY', 1, NOW()),
('txn-040', 550.00, 'USD', 'CARD', 'SUSPICIOUS', 'Swiggy', 'Suspicious', 'dev-003', 'CA', 2, NOW()),

('txn-041', 120.00, 'USD', 'UPI', 'RECOVERED', 'Flipkart', 'Recovered', 'dev-004', 'TX', 3, NOW()),
('txn-042', 900.00, 'USD', 'CARD', 'SUCCESS', 'Amazon', NULL, 'dev-005', 'NY', 1, NOW()),
('txn-043', 275.00, 'USD', 'NET BANKING', 'FAILED', 'Myntra', 'Failed', 'dev-001', 'CA', 2, NOW()),
('txn-044', 350.00, 'USD', 'UPI', 'TIMEOUT', 'Amazon', 'Timeout', 'dev-002', 'TX', 3, NOW()),
('txn-045', 650.00, 'USD', 'CARD', 'DECLINED', 'Swiggy', 'Declined', 'dev-003', 'NY', 1, NOW()),

('txn-046', 400.00, 'USD', 'WALLET', 'SUCCESS', 'Flipkart', NULL, 'dev-004', 'CA', 2, NOW()),
('txn-047', 250.00, 'USD', 'UPI', 'FAILED', 'Myntra', 'UPI failed', 'dev-005', 'TX', 3, NOW()),
('txn-048', 700.00, 'USD', 'NET BANKING', 'SUCCESS', 'Amazon', NULL, 'dev-001', 'NY', 1, NOW()),
('txn-049', 300.00, 'USD', 'CARD', 'SUSPICIOUS', 'Swiggy', 'Suspicious', 'dev-002', 'CA', 2, NOW()),
('txn-050', 500.00, 'USD', 'UPI', 'RECOVERED', 'Amazon', 'Recovered', 'dev-003', 'TX', 3, NOW());

-- Also add transaction events for the 50 transactions
INSERT INTO transaction_events (transaction_id, event_type, event_data, created_at) VALUES
-- Events for txn-001 to txn-050
('txn-001', 'TRANSACTION_CREATED', '{"eventType":"TRANSACTION_CREATED"}', NOW()),
('txn-002', 'TRANSACTION_CREATED', '{"eventType":"TRANSACTION_CREATED"}', NOW()),
('txn-003', 'TRANSACTION_CREATED', '{"eventType":"TRANSACTION_CREATED"}', NOW()),
('txn-004', 'TRANSACTION_CREATED', '{"eventType":"TRANSACTION_CREATED"}', NOW()),
('txn-005', 'TRANSACTION_CREATED', '{"eventType":"TRANSACTION_CREATED"}', NOW()),
('txn-006', 'TRANSACTION_FAILED', '{"eventType":"TRANSACTION_FAILED","failureReason":"Invalid card number"}', NOW()),
('txn-007', 'TRANSACTION_FAILED', '{"eventType":"TRANSACTION_FAILED","failureReason":"Insufficient funds"}', NOW()),
('txn-008', 'TRANSACTION_FAILED', '{"eventType":"TRANSACTION_FAILED","failureReason":"UPI PIN expired"}', NOW()),
('txn-009', 'TRANSACTION_TIMED_OUT', '{"eventType":"TRANSACTION_TIMED_OUT","reason":"Payment gateway timeout"}', NOW()),
('txn-010', 'TRANSACTION_TIMED_OUT', '{"eventType":"TRANSACTION_TIMED_OUT","reason":"Bank response timeout"}', NOW()),
('txn-011', 'TRANSACTION_DECLINED', '{"eventType":"TRANSACTION_DECLINED","reason":"Transaction declined by bank"}', NOW()),
('txn-012', 'TRANSACTION_DECLINED', '{"eventType":"TRANSACTION_DECLINED","reason":"Card declined"}', NOW()),
('txn-013', 'HIGH_RISK_FLAGGED', '{"eventType":"HIGH_RISK_FLAGGED","reason":"Large value transaction"}', NOW()),
('txn-014', 'HIGH_RISK_FLAGGED', '{"eventType":"HIGH_RISK_FLAGGED","reason":"High-risk merchant"}', NOW()),
('txn-015', 'HIGH_RISK_FLAGGED', '{"eventType":"HIGH_RISK_FLAGGED","reason":"Unusual pattern detected"}', NOW()),
('txn-016', 'TRANSACTION_RECOVERED', '{"eventType":"TRANSACTION_RECOVERED","reason":"Recovered after retry"}', NOW()),
('txn-017', 'TRANSACTION_RECOVERED', '{"eventType":"TRANSACTION_RECOVERED","reason":"Recovered after timeout"}', NOW()),
('txn-018', 'TRANSACTION_CREATED', '{"eventType":"TRANSACTION_CREATED"}', NOW()),
('txn-019', 'TRANSACTION_FAILED', '{"eventType":"TRANSACTION_FAILED","failureReason":"Card blocked"}', NOW()),
('txn-020', 'TRANSACTION_TIMED_OUT', '{"eventType":"TRANSACTION_TIMED_OUT","reason":"Gateway timeout"}', NOW()),
('txn-021', 'TRANSACTION_CREATED', '{"eventType":"TRANSACTION_CREATED"}', NOW()),
('txn-022', 'TRANSACTION_DECLINED', '{"eventType":"TRANSACTION_DECLINED","reason":"Insufficient UPI balance"}', NOW()),
('txn-023', 'HIGH_RISK_FLAGGED', '{"eventType":"HIGH_RISK_FLAGGED","reason":"Suspicious activity"}', NOW()),
('txn-024', 'TRANSACTION_RECOVERED', '{"eventType":"TRANSACTION_RECOVERED","reason":"Recovered after review"}', NOW()),
('txn-025', 'TRANSACTION_CREATED', '{"eventType":"TRANSACTION_CREATED"}', NOW()),
('txn-026', 'TRANSACTION_FAILED', '{"eventType":"TRANSACTION_FAILED","reason":"Risk flagged"}', NOW()),
('txn-027', 'TRANSACTION_SUCCESS', '{"eventType":"TRANSACTION_SUCCESS"}', NOW()),
('txn-028', 'TRANSACTION_TIMED_OUT', '{"eventType":"TRANSACTION_TIMED_OUT","reason":"Timeout"}', NOW()),
('txn-029', 'TRANSACTION_DECLINED', '{"eventType":"TRANSACTION_DECLINED","reason":"Declined"}', NOW()),
('txn-030', 'TRANSACTION_SUCCESS', '{"eventType":"TRANSACTION_SUCCESS"}', NOW()),
('txn-031', 'HIGH_RISK_FLAGGED', '{"eventType":"HIGH_RISK_FLAGGED","reason":"High risk"}', NOW()),
('txn-032', 'TRANSACTION_RECOVERED', '{"eventType":"TRANSACTION_RECOVERED","reason":"Recovered"}', NOW()),
('txn-033', 'TRANSACTION_FAILED', '{"eventType":"TRANSACTION_FAILED","reason":"Invalid card"}', NOW()),
('txn-034', 'TRANSACTION_SUCCESS', '{"eventType":"TRANSACTION_SUCCESS"}', NOW()),
('txn-035', 'TRANSACTION_DECLINED', '{"eventType":"TRANSACTION_DECLINED","reason":"Declined"}', NOW()),
('txn-036', 'TRANSACTION_SUCCESS', '{"eventType":"TRANSACTION_SUCCESS"}', NOW()),
('txn-037', 'TRANSACTION_FAILED', '{"eventType":"TRANSACTION_FAILED","reason":"Insufficient funds"}', NOW()),
('txn-038', 'TRANSACTION_TIMED_OUT', '{"eventType":"TRANSACTION_TIMED_OUT","reason":"Gateway timeout"}', NOW()),
('txn-039', 'TRANSACTION_DECLINED', '{"eventType":"TRANSACTION_DECLINED","reason":"Declined"}', NOW()),
('txn-040', 'HIGH_RISK_FLAGGED', '{"eventType":"HIGH_RISK_FLAGGED","reason":"Suspicious"}', NOW()),
('txn-041', 'TRANSACTION_RECOVERED', '{"eventType":"TRANSACTION_RECOVERED","reason":"Recovered"}', NOW()),
('txn-042', 'TRANSACTION_SUCCESS', '{"eventType":"TRANSACTION_SUCCESS"}', NOW()),
('txn-043', 'TRANSACTION_FAILED', '{"eventType":"TRANSACTION_FAILED","reason":"Failed"}', NOW()),
('txn-044', 'TRANSACTION_TIMED_OUT', '{"eventType":"TRANSACTION_TIMED_OUT","reason":"Timeout"}', NOW()),
('txn-045', 'TRANSACTION_DECLINED', '{"eventType":"TRANSACTION_DECLINED","reason":"Declined"}', NOW()),
('txn-046', 'TRANSACTION_SUCCESS', '{"eventType":"TRANSACTION_SUCCESS"}', NOW()),
('txn-047', 'TRANSACTION_FAILED', '{"eventType":"TRANSACTION_FAILED","reason":"UPI failed"}', NOW()),
('txn-048', 'TRANSACTION_SUCCESS', '{"eventType":"TRANSACTION_SUCCESS"}', NOW()),
('txn-049', 'HIGH_RISK_FLAGGED', '{"eventType":"HIGH_RISK_FLAGGED","reason":"Suspicious"}', NOW()),
('txn-050', 'TRANSACTION_RECOVERED', '{"eventType":"TRANSACTION_RECOVERED","reason":"Recovered"}', NOW());