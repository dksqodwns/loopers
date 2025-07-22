상품 (목록, 상세, 브랜드) 조회

상품 좋아요 등록, 취소 (멱등 동작)

주문 생성 및 결제 흐름 (재고 차감, 포인트 차감, 외부 시스템 연동)

시나리오 요구사항 전체 시스템

- 유저
    - 유저 회원가입 (POST /api/v1/users)
      : 유저는 개인정보를 입력하여 회원가입을 진행한다.

    - 유저 정보 조회 (GET /api/v1/users/me)
      : 유저는 X-USER-ID 헤더에 UserId를 담아 요청하여, 개인을 식별하고 정보를 조회 할 수 있다.

- 포인트
    - 포인트 충전 (POST /api/v1/points/charge)
      : 유저는 포인트를 충전 할 수 있다.

    - 포인트 조회 (GET /api/v1/points)
      : 유저는 개인의 포인트를 조회 할 수 있다.

- 상품 조회
    - 상품 목록 (GET /api/v1/products)
      : 상품 전체 목록을 조회 할 수 있다.

    - 상품 상세 (GET /api/v1/products/{productId})
      : 상품의 상세 정보를 조회 할 수 있다.

    - 상품 브랜드 (GET /api/v1/brands/{brandId})
      : 상품의 브랜드를 조회 할 수 있다.

- 상품 좋아요
    - 조회 (/api/v1/users/{userId}/likes)
      : 유저가 좋아요 한 상품 목록을 조회 할 수 있다.

    - 등록 (POST /api/v1/products/{productId}/likes)
      : 상품에 좋아요를 추가 할 수 있다.

    - 취소 (DELETE /api/v1/products/{productId}/likes)
      : 상품에 추가 한 좋아요를 취소 할 수 있다.

- 주문
    - 요청 (POST /api/v1/orders)
      : 유저는 상품을 주문 할 수 있다.

    - 내역 조회 (GET /api/v1/users/{userId}/orders)
      : 유저는 상품의 주문 내역을 확인 할 수 있다.

    - 상세 조회 (GET /api/v1/orders/{orderId})
      : 유저의 주문 내역을 조회 할 수있다.