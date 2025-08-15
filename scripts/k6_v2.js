import http from 'k6/http';
import {check, sleep} from 'k6';
import {Trend} from 'k6/metrics';

/* =========================
 * ENV 파라미터
 * ========================= */
const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const BASE_PATH = __ENV.BASE_PATH || '/api/v1/products';
const BRAND_PARAM = __ENV.BRAND_PARAM || 'brandId';
const MAX_BRAND_ID = parseInt(__ENV.MAX_BRAND_ID || '20210', 10);
const PAGE_SIZE = parseInt(__ENV.PAGE_SIZE || '20', 10);

// 시나리오 on/off & 목표 RPS
const ENABLE_BRAND = (__ENV.ENABLE_BRAND || '1') === '1';
const ENABLE_SORT = (__ENV.ENABLE_SORT || '1') === '1';
const BRAND_RPS = parseInt(__ENV.BRAND_RPS || '150', 10);
const SORT_RPS = parseInt(__ENV.SORT_RPS || '150', 10);

// VU 산정용 기대 p95(ms) & 헤드룸
// 인덱스 OFF 상태처럼 느릴 땐 6000~8000, ON 상태는 60~250 선으로 조정
const BRAND_P95_MS = parseInt(__ENV.BRAND_P95_MS || '60', 10);
const SORT_P95_MS = parseInt(__ENV.SORT_P95_MS || '60', 10);
const VU_HEADROOM = parseFloat(__ENV.VU_HEADROOM || '1.3'); // 여유율

// 임계치(필요 시 조정)
const FAIL_RATE_THRESH = __ENV.FAIL_RATE_THRESH || '0.01';
const BRAND_P95_THRESH = parseInt(__ENV.BRAND_P95_THRESH || '250', 10);
const SORT_P95_THRESH = parseInt(__ENV.SORT_P95_THRESH || '300', 10);

// 브랜드 선택: 고정 or 스큐 랜덤
const FIXED_BRAND_ID = __ENV.FIXED_BRAND_ID ? parseInt(__ENV.FIXED_BRAND_ID, 10) : null;
const BRAND_SKEW = parseFloat(__ENV.BRAND_SKEW || '2.0'); // 1=균등, 2~3=상위 쏠림

// 페이지 오프셋 분포 (CSV). 깊은 페이지 효과를 보려면 1000을 많이 넣기.
// 예: '0,0,10,100,500,1000,1000,1000'
const PAGES = (__ENV.PAGES || '0,0,0,10,10,100,500,1000').split(',').map(x => parseInt(x, 10));

/* =========================
 * 커스텀 메트릭
 * ========================= */
const brand_req_duration = new Trend('brand_req_duration'); // 전체(ms)
const brand_req_waiting = new Trend('brand_req_waiting');  // TTFB
const sort_req_duration = new Trend('sort_req_duration');
const sort_req_waiting = new Trend('sort_req_waiting');

/* =========================
 * 시나리오 옵션
 * ========================= */
function needVUs(targetRps, expectedP95ms, headroom) {
    // Little's Law 근사: VU ≈ RPS * 응답시간(초) * 헤드룸
    return Math.max(1, Math.ceil(targetRps * (expectedP95ms / 1000) * headroom));
}

function scenarioConfig(targetRps, expectedP95ms) {
    const preAllocated = needVUs(targetRps, expectedP95ms, VU_HEADROOM);
    const maxVUs = Math.ceil(preAllocated * 1.25);
    return {
        executor: 'ramping-arrival-rate',
        timeUnit: '1s',
        preAllocatedVUs: preAllocated,
        maxVUs: maxVUs,
        stages: [
            {duration: '30s', target: Math.floor(targetRps / 3)}, // 워밍업
            {duration: '60s', target: targetRps},                 // 상승
            {duration: '3m', target: targetRps},                 // 유지(측정)
            {duration: '30s', target: 0},                         // 쿨다운
        ],
    };
}

export const options = {
    scenarios: Object.assign(
        {},
        ENABLE_BRAND ? {brand_filter: {...scenarioConfig(BRAND_RPS, BRAND_P95_MS), exec: 'brandFilter'}} : {},
        ENABLE_SORT ? {like_sort: {...scenarioConfig(SORT_RPS, SORT_P95_MS), exec: 'likeSort'}} : {},
    ),
    thresholds: {
        // 에러율
        'http_req_failed{scenario:brand_filter}': [`rate<${FAIL_RATE_THRESH}`],
        'http_req_failed{scenario:like_sort}': [`rate<${FAIL_RATE_THRESH}`],
        // 지연 임계치(상황에 맞게 ENV로 조절)
        'brand_req_duration': [`p(95)<${BRAND_P95_THRESH}`],
        'sort_req_duration': [`p(95)<${SORT_P95_THRESH}`],
    },
    summaryTrendStats: ['min', 'avg', 'p(90)', 'p(95)', 'p(99)', 'max', 'count'],
};

/* =========================
 * 유틸
 * ========================= */
function pickBrandId() {
    if (FIXED_BRAND_ID) return FIXED_BRAND_ID;
    // 1..MAX_BRAND_ID 범위에서 스큐 랜덤
    const u = Math.pow(Math.random(), BRAND_SKEW);
    return 1 + Math.floor(u * Math.max(0, MAX_BRAND_ID - 1));
}

function pickPage() {
    return PAGES[Math.floor(Math.random() * PAGES.length)] || 0;
}

/* =========================
 * 시나리오 구현
 * ========================= */
export function brandFilter() {
    const brandId = pickBrandId();
    const page = pickPage();
    const url = `${BASE_URL}${BASE_PATH}?${BRAND_PARAM}=${brandId}&page=${page}&size=${PAGE_SIZE}`;
    const res = http.get(url, {tags: {name: 'brand_filter'}});

    brand_req_duration.add(res.timings.duration);
    brand_req_waiting.add(res.timings.waiting);

    check(res, {'brand 200': (r) => r.status === 200});
    sleep(0.05);
}

export function likeSort() {
    const page = pickPage();
    const url = `${BASE_URL}${BASE_PATH}?sort=likeCount,desc&page=${page}&size=${PAGE_SIZE}`;
    const res = http.get(url, {tags: {name: 'like_sort'}});

    sort_req_duration.add(res.timings.duration);
    sort_req_waiting.add(res.timings.waiting);

    check(res, {'sort 200': (r) => r.status === 200});
    sleep(0.05);
}
