```mysql

EXPLAIN ANALYZE
SELECT id, name, price, like_count
FROM products
ORDER BY like_count DESC, id DESC
LIMIT 20 OFFSET 1000;
```

-> Limit/Offset: 20/1000 row(s)  (cost=20193 rows=20) (actual time=89.6..89.6 rows=20 loops=1)
-> Sort: products.like_count DESC, products.id DESC, limit input to 1020 row(s) per chunk  (cost=20193 rows=199280) (
actual time=89.6..89.6 rows=1020 loops=1)
-> Table scan on products  (cost=20193 rows=199280) (actual time=0.188..47 rows=199999 loops=1)

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
      "using_filesort": true,
      "table": {
        "table_name": "products",
        "access_type": "ALL",
        "rows_examined_per_scan": 199280,
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
