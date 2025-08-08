package com.loopers.domain.product;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductInfo getProduct(ProductCommand.GetProduct command) {
        Product product = this.productRepository.findById(command.id()).orElseThrow(
                () -> new CoreException(ErrorType.NOT_FOUND, "해당하는 상품을 찾을 수 없습니다. ID: " + command.id())
        );
        return ProductInfo.from(product);
    }

    public Optional<ProductInfo> getProductById(ProductCommand.GetProduct command) {
        return productRepository.findById(command.id())
                .map(ProductInfo::from);
    }

    @Transactional(readOnly = true)
    public Page<ProductInfo> searchProducts(final ProductCommand.SerachProducts command) {
        if (command.brandId() == null) {
            return productRepository.findAllBy(command.pageRequest())
                    .map(ProductInfo::from);
        }

        return productRepository.findAllBy(command.brandId(), command.pageRequest())
                .map(ProductInfo::from);
    }


    @Transactional(readOnly = true)
    public List<ProductInfo> getProducts(final ProductCommand.GetProducts command) {
        return productRepository.findAllByIds(command.ids()).stream()
                .map(ProductInfo::from)
                .toList();
    }


}
