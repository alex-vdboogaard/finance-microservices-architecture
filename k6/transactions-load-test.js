import { runTransactionScenario } from './transactions-utils.js';

// Steady moderate load to validate normal behaviour under expected traffic
export const options = {
  vus: 30,
  duration: '5m',
};

export default function () {
  runTransactionScenario();
}

