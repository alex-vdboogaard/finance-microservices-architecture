-- Repeatable Seed Data Script for Transaction Service
-- NOTE FOR DEVELOPERS: The rows inserted below are mock/test data intended strictly for development and testing environments.

INSERT INTO transaction (id, from_account_id, to_account_id, amount, status, description, created_at, updated_at) VALUES
('tx-100001-mock-uuid', 5001, 5003, 150.00, 'SUCCESS', '[TEST DATA] Initial deposit transfer from account 5001 to 5003', '2026-08-01 10:00:00', '2026-08-01 10:00:00'),
('tx-100002-mock-uuid', 5003, 5002, 50.25, 'SUCCESS', '[TEST DATA] Peer-to-peer payment from account 5003 to 5002', '2026-08-05 14:30:00', '2026-08-05 14:30:00'),
('tx-100003-mock-uuid', 5002, 5004, 200.00, 'FAILED', '[TEST DATA] Transfer failed due to inactive target account 5004', '2026-08-10 09:15:00', '2026-08-10 09:15:00'),
('tx-100004-mock-uuid', 5001, 5002, 75.00, 'PENDING', '[TEST DATA] Pending scheduled transfer from account 5001 to 5002', '2026-08-15 18:00:00', '2026-08-15 18:00:00')
ON CONFLICT (id) DO NOTHING;
