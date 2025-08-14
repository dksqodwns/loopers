package com.loopers.application.product;

import com.loopers.domain.count.ProductCountCommand;
import com.loopers.domain.product.ProductCommand;
import com.loopers.domain.product.ProductCommand.SerachProducts;
import com.loopers.domain.stock.ProductStockCommand;
import com.loopers.support.enums.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ProductCriteria {

    public record SearchProducts(Long brandId, PageRequest pageRequest) {
        public static SearchProducts of(final Long brandId, final Pageable pageable) {
            final PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
            return new SearchProducts(brandId, pageRequest);
        }

        public SerachProducts toCommand() {
            return new SerachProducts(brandId, pageRequest);
        }
    }

    public record GetProduct(Long productId) {
        public ProductCommand.GetProduct toProductCommand() {
            return new ProductCommand.GetProduct(productId);
        }

        public ProductStockCommand.GetStock toStockCommand() {
            return new ProductStockCommand.GetStock(productId);
        }

        public ProductCountCommand.GetProductCount toCountCommand() {
            return new ProductCountCommand.GetProductCount(productId);
        }
    }
}
