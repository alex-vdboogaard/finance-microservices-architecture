import { runTransactionScenario } from './transactions-utils.js';

// Long-running, gradually increasing load to observe scaling behaviour
export const options = {
  stages: [
    { duration: '5m', target: 20 },
    { duration: '5m', target: 40 },
    { duration: '5m', target: 60 },
    { duration: '5m', target: 80 },
    { duration: '5m', target: 100 },
    { duration: '5m', target: 0 },
  ],
};

export default function () {
  runTransactionScenario();
}

