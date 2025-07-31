package com.loopers.infrastructure.product;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private ProductJpaRepository productJpaRepository;

    @Override
    public Product save(Product product) {
        return this.productJpaRepository.save(product);
    }

    @Override
    public List<Product> findAll() {
        return List.of();
    }

    @Override
    public Optional<Product> findByProductId(Long productId) {
        return this.productJpaRepository.findById(productId);
    }
}
