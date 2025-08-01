package com.loopers.domain.product;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ProductRepository {
    Product save(Product product);

    List<Product> findAllByIdList(List<Long> ids);


    Optional<Product> findByProductId(Long productId);

    Page<Product> findAll(PageRequest pageRequest);
}
