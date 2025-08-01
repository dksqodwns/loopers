package com.loopers.application.product;

import java.util.List;

public class ProductCommand {
    public record GetProduct(Long productId) { }

    public record GetProductList(List<Long> productIdList) { }

    public record GetProductStockList(List<Long> productIdList) { }

    public record DecreaseStock(Long productId, Integer quantity) { }
}
