-- Repeatable Seed Data Script for Notification Service
-- NOTE FOR DEVELOPERS: The rows inserted below are mock/test data intended strictly for development and testing environments.

INSERT INTO notification (user_id, title, description, timestamp) VALUES
(1001, '[TEST DATA] Welcome to Finance App', '[TEST DATA] Initial onboarding notification for developer test user 1001', '2026-08-01 10:00:00'),
(1001, '[TEST DATA] Deposit Received', '[TEST DATA] Dev test transaction: Received mock deposit of $500.00', '2026-08-05 14:30:00'),
(1002, '[TEST DATA] Security Alert', '[TEST DATA] Mock security login event triggered for dev testing', '2026-08-10 09:15:00'),
(1003, '[TEST DATA] Monthly Statement Ready', '[TEST DATA] Developer test notification for monthly statement availability', '2026-08-15 18:00:00'),
(1004, '[TEST DATA] Password Changed', '[TEST DATA] Test data entry: User account password was updated in sandbox environment', '2026-08-20 12:45:00');
