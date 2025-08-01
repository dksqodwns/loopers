package com.loopers.domain.product;

import com.loopers.domain.product.ProductWithLikeCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);

    List<Product> findAllByIdList(List<Long> ids);

    Optional<Product> findByProductId(Long productId);

    Page<Product> findAll(PageRequest pageRequest);

    Page<ProductWithLikeCount> findAllWithLikeCount(PageRequest pageRequest);
}
