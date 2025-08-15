```mysql

-- 얕은 페이지
EXPLAIN FORMAT = JSON
SELECT id, name, price, like_count
FROM products
WHERE ref_brand_id = 1
ORDER BY created_at DESC, id DESC
LIMIT 20 OFFSET 0;
```

```json
{
  "query_block": {
    "select_id": 1,
    "cost_info": {
      "query_cost": "2232.25"
    },
    "ordering_operation": {
      "using_filesort": true,
      "table": {
        "table_name": "products",
        "access_type": "ref",
        "possible_keys": [
          "idx_products_ref_brand_id"
        ],
        "key": "idx_products_ref_brand_id",
        "used_key_parts": [
          "ref_brand_id"
        ],
        "key_length": "8",
        "ref": [
          "const"
        ],
        "rows_examined_per_scan": 14380,
        "rows_produced_per_join": 14380,
        "filtered": "100.00",
        "cost_info": {
          "read_cost": "794.25",
          "eval_cost": "1438.00",
          "prefix_cost": "2232.25",
          "data_read_per_join": "14M"
        },
        "used_columns": [
          "id",
          "ref_brand_id",
          "name",
          "price",
          "like_count",
          "created_at"
        ]
      }
    }
  }
}
```

```mysql
-- 깊은 페이지 (정렬/스킵 비용 확인)
EXPLAIN FORMAT = JSON
SELECT id, name, price, like_count
FROM products
WHERE ref_brand_id = 1
ORDER BY created_at DESC, id DESC
LIMIT 20 OFFSET 1000;
```

```json

{
  "query_block": {
    "select_id": 1,
    "cost_info": {
      "query_cost": "2232.25"
    },
    "ordering_operation": {
      "using_filesort": true,
      "table": {
        "table_name": "products",
        "access_type": "ref",
        "possible_keys": [
          "idx_products_ref_brand_id"
        ],
        "key": "idx_products_ref_brand_id",
        "used_key_parts": [
          "ref_brand_id"
        ],
        "key_length": "8",
        "ref": [
          "const"
        ],
        "rows_examined_per_scan": 14380,
        "rows_produced_per_join": 14380,
        "filtered": "100.00",
        "cost_info": {
          "read_cost": "794.25",
          "eval_cost": "1438.00",
          "prefix_cost": "2232.25",
          "data_read_per_join": "14M"
        },
        "used_columns": [
          "id",
          "ref_brand_id",
          "name",
          "price",
          "like_count",
          "created_at"
        ]
      }
    }
  }
}
```

```mysql
-- 가능하면 MySQL 8.0.18+에서는 실제 실행 시간까지
EXPLAIN ANALYZE
SELECT id, name, price, like_count
FROM products
WHERE ref_brand_id = 1
ORDER BY created_at DESC, id DESC
LIMIT 20 OFFSET 1000;
```

-> Limit/Offset: 20/1000 row(s)  (cost=2232 rows=20) (actual time=24.6..24.6 rows=20 loops=1)
-> Sort: products.created_at DESC, products.id DESC, limit input to 1020 row(s) per chunk  (cost=2232 rows=14380) (
actual time=24.5..24.6 rows=1020 loops=1)
-> Index lookup on products using idx_products_ref_brand_id (ref_brand_id=1)  (cost=2232 rows=14380) (actual
time=0.958..19.3 rows=7990 loops=1)
