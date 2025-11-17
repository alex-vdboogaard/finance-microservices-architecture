import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';

const BASE_URL = 'http://localhost:8080/api/v1/logs';

// From api-gateway/src/main/resources/application.yml
// redis-rate-limiter.replenishRate: 1000 req/s
// redis-rate-limiter.burstCapacity: 2000
const REPLENISH_RATE = 1000;

export const options = {
  stages: [
    // ~20 iterations/s per VU with sleep(0.05)
    // 50 VUs * 20 req/s ≈ 1000 req/s (matches gateway rate limit)
    { duration: '5s', target: REPLENISH_RATE / 20 }, // ramp up to target VUs
    { duration: '10s', target: REPLENISH_RATE / 20 }, // hold at ~rate limit
    { duration: '5s', target: 0 }, // ramp down
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
