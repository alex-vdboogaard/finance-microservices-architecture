import { runTransactionScenario } from './transactions-utils.js';

// Gradually increase load to find breaking point
export const options = {
  stages: [
    { duration: '2m', target: 20 },
    { duration: '2m', target: 40 },
    { duration: '2m', target: 80 },
    { duration: '2m', target: 120 },
    { duration: '2m', target: 0 },
  ],
};

export default function () {
  runTransactionScenario();
}

