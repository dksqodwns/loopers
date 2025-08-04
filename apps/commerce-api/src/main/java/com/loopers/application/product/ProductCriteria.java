package com.loopers.application.product;

import javax.swing.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ProductCriteria {

    public record GetProducts(Long brandId, PageRequest pageRequest) {
        public static GetProducts of(final Long brandId, final String sort, final SortOrder sortOrder, final Pageable pageable) {
            final Sort.Direction direction = Sort.Direction.fromString(sortOrder.name());
            final PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by(direction, sort));

            return new GetProducts(brandId, pageRequest);
        }

        public ProductCommand.GetProducts toCommand() {
            return new ProductCommand.GetProducts(brandId, pageRequest);
        }
    }

    public record GetProduct(Long productId) {
        public ProductCommand.GetProduct toProductCommand() {
            return new ProductCommand.GetProduct(productId);
        }

        public ProductCommand.GetStock toStockCommand() {
            return new ProductCommand.GetStock(productId);
        }

        public ProductCountCommand.GetProductCount toCountCommand() {
            return new ProductCountCommand.GetProductCount(productId);
        }
    }
}
