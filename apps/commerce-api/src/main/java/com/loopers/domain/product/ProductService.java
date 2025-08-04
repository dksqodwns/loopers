package com.loopers.domain.product;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductInfo getProduct(Long productId) {
        Product product = this.productRepository.findById(productId).orElseThrow(
                () -> new CoreException(ErrorType.NOT_FOUND, "해당하는 상품을 찾을 수 없습니다. ID: " + productId)
        );

        return ProductInfo.from(product);
    }

}
