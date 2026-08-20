-- Repeatable Seed Data Script for Audit Log Service
-- NOTE FOR DEVELOPERS: The rows inserted below are mock/test data intended strictly for development and testing environments.

INSERT INTO audit_log (action, timestamp) VALUES
('[TEST DATA] USER_LOGIN_SUCCESS - Dev test user 1001 logged in from localhost', '2026-08-01 10:00:00'),
('[TEST DATA] ACCOUNT_DEPOSIT - Dev test deposit of $500.00 on account 5001', '2026-08-05 14:30:00'),
('[TEST DATA] USER_PASSWORD_RESET - Dev test password reset request for user 1002', '2026-08-10 09:15:00'),
('[TEST DATA] TRANSFER_EXECUTE - Dev test fund transfer from account 5001 to 5002', '2026-08-15 18:00:00'),
('[TEST DATA] USER_PROFILE_UPDATE - Dev test profile update action performed in sandbox', '2026-08-20 12:45:00');
