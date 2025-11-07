import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';

const BASE_URL = 'http://localhost:8080/audit-log-service/api/v1/logs';

export const options = {
  stages: [
    { duration: '2s', target: 100 },  // ramp up to 100 users
    { duration: '2s', target: 100 }, // hold
    { duration: '1s', target: 0 },    // ramp down
  ],
};

const success = new Counter('success_count');
const not_found = new Counter('not_found_count');
const throttled = new Counter('throttled_count');
const other = new Counter('other_count');

export default function () {
  const res = http.get(BASE_URL);

  if (res.status === 200) success.add(1);
  else if (res.status === 404) not_found.add(1);
  else if (res.status === 429) throttled.add(1);
  else other.add(1);

  check(res, { 'status 200 or 404 or 429': (r) => r.status === 200 || r.status === 429 || r.status === 404 });

  sleep(0.05); // short delay between requests
}
