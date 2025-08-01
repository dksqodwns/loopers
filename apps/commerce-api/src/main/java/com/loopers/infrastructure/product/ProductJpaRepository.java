package com.loopers.infrastructure.product;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductWithLikeCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT new com.loopers.domain.product.ProductWithLikeCount(p, COUNT(l) as likeCount) " +
               "FROM Product p LEFT JOIN Like l ON p.id = l.productId " +
               "GROUP BY p.id",
       countQuery = "SELECT count(p) FROM Product p")
    Page<ProductWithLikeCount> findAllWithLikeCount(Pageable pageable);
}
