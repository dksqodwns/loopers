import http from 'k6/http';
import {check, sleep} from 'k6';
import {Trend} from 'k6/metrics';

/** ==== 환경변수 ==== **/
const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const BRAND_PARAM = __ENV.BRAND_PARAM || 'brandId'; // 필요시 'brand'로 바꿔서 실행
const MAX_BRAND_ID = parseInt(__ENV.MAX_BRAND_ID || '20210', 10); // 보유 브랜드 수에 맞게
const PAGE_SIZE = parseInt(__ENV.PAGE_SIZE || '20', 10);

// 시나리오 on/off & 목표 RPS
const ENABLE_BRAND = (__ENV.ENABLE_BRAND || '1') === '1';
const ENABLE_SORT = (__ENV.ENABLE_SORT || '1') === '1';
const BRAND_RPS = parseInt(__ENV.BRAND_RPS || '150', 10);
const SORT_RPS = parseInt(__ENV.SORT_RPS || '150', 10);

// 페이지 깊이 샘플
const PAGES = (__ENV.PAGES || '0,0,0,10,10,100,500,1000').split(',').map(Number);

// 브랜드 쏠림(1=균등, 2~3=상위에 몰림)
const BRAND_SKEW = parseFloat(__ENV.BRAND_SKEW || '2.0');

// ==== 커스텀 메트릭(시나리오 분리) ====
const m_brand_dur = new Trend('brand_req_duration');  // ms
const m_brand_wait = new Trend('brand_req_waiting');   // TTFB
const m_sort_dur = new Trend('sort_req_duration');
const m_sort_wait = new Trend('sort_req_waiting');

// ==== k6 옵션 ====
function scenarioConfig(targetRps) {
    return {
        executor: 'ramping-arrival-rate',
        timeUnit: '1s',
        preAllocatedVUs: Math.max(50, targetRps),
        maxVUs: Math.max(200, targetRps * 3),
        stages: [
            {duration: '30s', target: Math.floor(targetRps / 3)}, // warm-up
            {duration: '60s', target: targetRps},
            {duration: '3m', target: targetRps},               // measure window
            {duration: '30s', target: 0},
        ],
    };
}

export const options = {
    scenarios: Object.assign(
        {},
        ENABLE_BRAND ? {brand_filter: {...scenarioConfig(BRAND_RPS), exec: 'brandFilter'}} : {},
        ENABLE_SORT ? {like_sort: {...scenarioConfig(SORT_RPS), exec: 'likeSort'}} : {},
    ),
    thresholds: {
        // 에러율
        'http_req_failed{scenario:brand_filter}': ['rate<0.01'],
        'http_req_failed{scenario:like_sort}': ['rate<0.01'],
        // 커스텀 메트릭 목표 (상황에 맞게 조정)
        'brand_req_duration': ['p(95)<250'],
        'sort_req_duration': ['p(95)<300'],
    },
    summaryTrendStats: ['min', 'avg', 'p(90)', 'p(95)', 'p(99)', 'max', 'count'],
};

// ==== 유틸 ====
function skewedBrandId() {
    const u = Math.pow(Math.random(), BRAND_SKEW);
    return 1 + Math.floor(u * (MAX_BRAND_ID - 1));
}

function randomPage() {
    return PAGES[Math.floor(Math.random() * PAGES.length)];
}

// ==== 시나리오 ====
export function brandFilter() {
    const brandId = skewedBrandId();
    const page = randomPage();
    const url = `${BASE_URL}/api/v1/products?${BRAND_PARAM}=${brandId}&page=${page}&size=${PAGE_SIZE}`;
    const res = http.get(url, {tags: {name: 'brand_filter'}});

    m_brand_dur.add(res.timings.duration);
    m_brand_wait.add(res.timings.waiting);

    check(res, {'brand 200': r => r.status === 200});
    sleep(0.05);
}

export function likeSort() {
    const page = randomPage();
    const url = `${BASE_URL}/api/v1/products?sort=likeCount,desc&page=${page}&size=${PAGE_SIZE}`;
    const res = http.get(url, {tags: {name: 'like_sort'}});

    m_sort_dur.add(res.timings.duration);
    m_sort_wait.add(res.timings.waiting);

    check(res, {'sort 200': r => r.status === 200});
    sleep(0.05);
}
