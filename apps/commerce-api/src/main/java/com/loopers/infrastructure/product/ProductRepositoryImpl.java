package com.loopers.infrastructure.product;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;

    @Override
    public Product save(Product product) {
        return this.productJpaRepository.save(product);
    }

    @Override
    public List<Product> findAllByIdList(List<Long> ids) {
        return this.productJpaRepository.findAllById(ids);
    }

    public Page<Product> findAll(Pageable pageable) {
        return this.productJpaRepository.findAll(pageable);
    }

    @Override
    public Optional<Product> findByProductId(Long productId) {
        return this.productJpaRepository.findById(productId);
    }
}
