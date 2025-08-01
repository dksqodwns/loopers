package com.loopers.infrastructure.product;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.product.ProductWithLikeCount;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public Page<Product> findAll(PageRequest pageRequest) {
        return productJpaRepository.findAll(pageRequest);
    }

    @Override
    public List<Product> findAllByIdList(List<Long> ids) {
        return this.productJpaRepository.findAllById(ids);
    }

    @Override
    public Optional<Product> findByProductId(Long productId) {
        return this.productJpaRepository.findById(productId);
    }

    @Override
    public Page<ProductWithLikeCount> findAllWithLikeCount(PageRequest pageRequest) {
        return productJpaRepository.findAllWithLikeCount(pageRequest);
    }
}
