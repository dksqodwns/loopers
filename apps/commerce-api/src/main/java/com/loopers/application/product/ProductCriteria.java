package com.loopers.application.product;

import com.loopers.application.product.ProductCommand.GetProductList;
import com.loopers.interfaces.api.product.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record ProductCriteria {
    public record ProductList(PageRequest pageRequest) {
        public static ProductList from(String filed, SortOrder sortOrder, Pageable pageable) {
            Sort.Direction direction = Sort.Direction.fromString(sortOrder.name());
            PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by(direction, filed));

            return new ProductList(pageRequest);
        }

        public ProductCommand.GetProductList toGetProductListCommand() {
            return new GetProductList(pageRequest);
        }
    }
}
