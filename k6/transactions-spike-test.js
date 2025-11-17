import { runTransactionScenario } from './transactions-utils.js';

// Sudden large spike in traffic to test shock absorption
export const options = {
  stages: [
    { duration: '1m', target: 10 },   // baseline
    { duration: '10s', target: 150 }, // sudden spike
    { duration: '2m', target: 150 },  // sustain spike briefly
    { duration: '1m', target: 10 },   // drop back to near-baseline
    { duration: '1m', target: 0 },
  ],
};

export default function () {
  runTransactionScenario();
}

