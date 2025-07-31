package com.loopers.domain.product;

import com.loopers.application.product.ProductCommand.Get;
import com.loopers.application.product.ProductInfo;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public Optional<ProductInfo> getProductById(Get command) {
        return this.productRepository.findByProductId(command.productId()).flatMap(
                product -> Optional.of(ProductInfo.from(product))
        );
    }

}
