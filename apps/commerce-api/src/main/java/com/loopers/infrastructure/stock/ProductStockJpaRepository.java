package com.loopers.infrastructure.stock;

import com.loopers.domain.stock.ProductStock;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStockJpaRepository extends JpaRepository<ProductStock, Long> {
    Optional<ProductStock> findByProductId(Long productId);

    List<ProductStock> findAllByProductIds(List<Long> productIds);
}
