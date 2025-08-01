package com.loopers.application.product;

import com.loopers.application.product.ProductCommand.GetProduct;
import com.loopers.application.product.ProductInfo.ProductListInfo;
import com.loopers.domain.product.ProductService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductFacade {
    private final ProductService productService;

    public ProductInfo getProduct(GetProduct command) {
        return productService.getProductById(command).orElseThrow(
                () -> new CoreException(ErrorType.NOT_FOUND, "해당 상품 혹은 브랜드 정볼르 조회 할 수 없습니다. productId: " + command.productId())
        );
    }

    public ProductListInfo getProductList(ProductCriteria.ProductList criteria) {
        Page<ProductInfo> productInfoList = productService.getProductList(criteria.toGetProductListCommand());

        return ProductListInfo.from(productInfoList);
    }

}
