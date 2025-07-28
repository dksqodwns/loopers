```mermaid
---
title: 상품 조회 기능
---

classDiagram
    class Product {
        - Long id
        - String name
        - Integer price
        - String image
        - Brand brand
    }

    class Brand {
        - Long id
        - String name
    }

    class ProductLike {
        - Long id
        - Member member
        - Product product
    }

    class Member {
        - Long id
    }

    Member "1" -- "N" ProductLike: 소유
    Product "1" -- "N" ProductLike: 참조
    Brand "1" -- "N" Product: 소유
```

```mermaid
---
title: 상품 좋아요 기능
---

classDiagram
    class Product {
        - Long id
    }
    
    class Member {
        - Long id
        + like(Product) 
        + disLike(Product)
    }
    
    class ProductLike {
        - Long id
        - Member member
        - Product product
    }

    Member "1" -- "N" ProductLike: 소유
    Product "1" -- "N" ProductLike: 참조
```

```mermaid
---
title: 주문 기능
---

classDiagram
    class Product {
        - Long id
        - String name
        - int price
        - int stock
        + calculateStock()
        + decreaseStock()
    }

    class Member {
        - Long id
        - Point point
        + buy(OrderProduct)
    }

    class Order {
        - Long id
        - Member member
        - OrderProduct orderProducts
        - OrderStatus orderStatus
        + calculateTotalPrice(OrderProduct)
        + create(Member, OrderProduct)
    }

    class OrderStatus {
        <<enumeration>>
        PENDING
        CANCELED
        COMPLETED
    }

    class OrderProduct {
        - Long id
        - Product product
        - int quantity
    }

    Member "1" -- "N" Order: 참조
    Order "1" -- "N" OrderProduct: 포함
    OrderProduct "1" -- "N" Product: 포함
    Order  --  OrderStatus: 상태
```