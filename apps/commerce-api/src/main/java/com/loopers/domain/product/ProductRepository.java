package com.loopers.domain.product;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {
    Product save(Product product);

    Page<Product> findAll(Pageable pageable);

    Optional<Product> findByProductId(Long productId);
}
