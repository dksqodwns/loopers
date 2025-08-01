package com.loopers.domain.product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);

    List<Product> findAllByIdList(List<Long> ids);


    Optional<Product> findByProductId(Long productId);
}
