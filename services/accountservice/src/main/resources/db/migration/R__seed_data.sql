-- Repeatable Seed Data Script for Account Service
-- NOTE FOR DEVELOPERS: The rows inserted below are mock/test data intended strictly for development and testing environments.

INSERT INTO users (user_id, first_name, last_name, email, goverment_id, password) VALUES
(1001, 'Alice', 'Smith', 'alice.smith@example.com', '1234567890', 'password123'),
(1002, 'Bob', 'Johnson', 'bob.johnson@example.com', '0987654321', 'password123'),
(1003, 'Charlie', 'Brown', 'charlie.brown@example.com', '1122334455', 'password123')
ON CONFLICT (email) DO NOTHING;

-- Synchronize the users sequence if IDs were explicitly inserted
SELECT setval('users_user_id_seq', (SELECT MAX(user_id) FROM users));

INSERT INTO accounts (id, user_id, account_number, balance, status, created_at, updated_at) VALUES
(5001, 1001, 'ACC-1001-01', 1500.50, 'ACTIVE', '2026-08-01 10:00:00', '2026-08-01 10:00:00'),
(5002, 1001, 'ACC-1001-02', 300.00, 'ACTIVE', '2026-08-05 14:30:00', '2026-08-05 14:30:00'),
(5003, 1002, 'ACC-1002-01', 2750.75, 'ACTIVE', '2026-08-10 09:15:00', '2026-08-10 09:15:00'),
(5004, 1003, 'ACC-1003-01', 100.00, 'INACTIVE', '2026-08-15 18:00:00', '2026-08-15 18:00:00')
ON CONFLICT (id) DO NOTHING;

-- Synchronize the accounts sequence if IDs were explicitly inserted
SELECT setval('accounts_id_seq', (SELECT MAX(id) FROM accounts));
