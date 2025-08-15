```mysql
EXPLAIN ANALYZE
SELECT id, name, price, like_count
FROM products
ORDER BY like_count DESC, id DESC
LIMIT 20 OFFSET 1000;

```

-> Limit/Offset: 20/1000 row(s)  (cost=1.88 rows=20) (actual time=10.7..10.7 rows=20 loops=1)
-> Index scan on products using idx_products_like_count_desc  (cost=1.88 rows=1020) (actual time=2.36..10.6 rows=1020
loops=1)

```mysql
EXPLAIN FORMAT = JSON
SELECT id, name, price, like_count
FROM products
ORDER BY like_count DESC, id DESC
LIMIT 20 OFFSET 1000;
```

```json
{
  "query_block": {
    "select_id": 1,
    "cost_info": {
      "query_cost": "20192.75"
    },
    "ordering_operation": {
      "using_filesort": false,
      "table": {
        "table_name": "products",
        "access_type": "index",
        "key": "idx_products_like_count_desc",
        "used_key_parts": [
          "like_count",
          "id"
        ],
        "key_length": "16",
        "rows_examined_per_scan": 1020,
        "rows_produced_per_join": 199280,
        "filtered": "100.00",
        "cost_info": {
          "read_cost": "264.75",
          "eval_cost": "19928.00",
          "prefix_cost": "20192.75",
          "data_read_per_join": "203M"
        },
        "used_columns": [
          "id",
          "name",
          "price",
          "like_count"
        ]
      }
    }
  }
}
```
