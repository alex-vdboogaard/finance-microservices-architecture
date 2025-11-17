import http from 'k6/http';
import { check, sleep } from 'k6';

export const BASE_URL = 'http://localhost:8080/api/v1/transactions';

// Seeded accounts from docs/db/account.sql (2 accounts per 20 users = IDs 1–40)
const VALID_ACCOUNT_IDS = Array.from({ length: 40 }, (_, i) => i + 1);
const INVALID_ACCOUNT_IDS = [999999, 888888];

function randomItem(arr) {
  return arr[Math.floor(Math.random() * arr.length)];
}

function randomValidAccounts() {
  let fromAccountId = randomItem(VALID_ACCOUNT_IDS);
  let toAccountId = randomItem(VALID_ACCOUNT_IDS);

  // Avoid same-account transfers for "valid" scenarios
  while (toAccountId === fromAccountId) {
    toAccountId = randomItem(VALID_ACCOUNT_IDS);
  }

  return { fromAccountId, toAccountId };
}

// Function to generate a random transaction
export function randomTransaction() {
  const { fromAccountId: baseFrom, toAccountId: baseTo } = randomValidAccounts();
  let fromAccountId = baseFrom;
  let toAccountId = baseTo;

  // Base valid amount between 1 and 1000
  let amount = parseFloat((Math.random() * 1000 + 1).toFixed(2));

  // Decide scenario:
  // - 1% negative amount
  // - 5% account does not exist
  // - 5% zero amount
  // - 5% amount above max allowed (100000)
  // - remaining: valid
  const roll = Math.random();
  let scenario = 'valid';

  if (roll < 0.01) {
    amount = -amount;
    scenario = 'negative_amount';
  } else if (roll < 0.06) {
    const invalidId = randomItem(INVALID_ACCOUNT_IDS);
    if (Math.random() < 0.5) {
      fromAccountId = invalidId;
    } else {
      toAccountId = invalidId;
    }
    scenario = 'nonexistent_account';
  } else if (roll < 0.11) {
    amount = 0;
    scenario = 'zero_amount';
  } else if (roll < 0.16) {
    amount = parseFloat((100001 + Math.random() * 100000).toFixed(2));
    scenario = 'too_large_amount';
  }

  return {
    payload: { fromAccountId, toAccountId, amount },
    scenario,
  };
}

export function runTransactionScenario() {
  const { payload, scenario } = randomTransaction();
  const payloadJson = JSON.stringify(payload);
  const params = {
    headers: { 'Content-Type': 'application/json' },
  };

  const res = http.post(BASE_URL, payloadJson, params);

  check(res, {
    'request has valid HTTP status': (r) => r.status > 0 && r.status < 600,
    'valid transaction returns 201 or 200': (r) =>
      scenario === 'valid' ? r.status === 201 || r.status === 200 : true,
    'validation errors return 400': (r) =>
      (scenario === 'negative_amount' ||
        scenario === 'zero_amount' ||
        scenario === 'too_large_amount')
        ? r.status === 400
        : true,
    'nonexistent account handled (4xx or 201)': (r) =>
      scenario === 'nonexistent_account'
        ? (r.status === 404 || r.status === 400 || r.status === 201)
        : true,
  });

  sleep(0.2);
}

