package com.loopers.application.product;

import java.util.List;
import org.springframework.data.domain.PageRequest;

public class ProductCommand {
    public record GetProduct(Long productId) { }

    public record GetProductList(PageRequest pageRequest) {}

    public record GetProductListById(List<Long> productIdList) { }

    public record GetProductStockList(List<Long> productIdList) { }

    public record DecreaseStock(Long productId, Integer quantity) { }
}
