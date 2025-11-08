import http from 'k6/http';
import { check, sleep } from 'k6';

// Configuration
export const options = {
  vus: 30,          // 30 concurrent virtual users
  iterations: 300,  // total 300 requests
};

const BASE_URL = 'http://localhost:8080/api/v1/transactions';
const ACCOUNT_IDS = [1001, 1002, 1003, 1004, 1005];

// Function to generate a random transaction
function randomTransaction() {
  const fromAccountId = ACCOUNT_IDS[Math.floor(Math.random() * ACCOUNT_IDS.length)];
  let toAccountId = ACCOUNT_IDS[Math.floor(Math.random() * ACCOUNT_IDS.length)];

  // Avoid same account transfers
  while (toAccountId === fromAccountId) {
    toAccountId = ACCOUNT_IDS[Math.floor(Math.random() * ACCOUNT_IDS.length)];
  }

  // Random amount: 90% positive, 10% negative
  let amount = parseFloat((Math.random() * 1000).toFixed(2));
  if (Math.random() < 0.1) amount = -amount; // fraudulent case

  return { fromAccountId, toAccountId, amount };
}

export default function () {
  const tx = randomTransaction();
  const payload = JSON.stringify(tx);
  const params = {
    headers: { 'Content-Type': 'application/json' },
  };

  const res = http.post(BASE_URL, payload, params);

  check(res, {
    'status is 200 or 201': (r) => r.status === 200 || r.status === 201,
    'rejects fraudulent (400 or 422)': (r) =>
      tx.amount < 0 ? r.status === 400 || r.status === 422 : true,
  });

  sleep(0.2);
}
