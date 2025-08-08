package com.loopers.infrastructure.stock;

import com.loopers.domain.stock.ProductStock;
import com.loopers.domain.stock.ProductStockRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductStockRepositoryImpl implements ProductStockRepository {
    private final ProductStockJpaRepository productStockJpaRepository;

    @Override
    public Optional<ProductStock> findByProductId(Long productId) {
        return this.productStockJpaRepository.findByProductId(productId);
    }

}
