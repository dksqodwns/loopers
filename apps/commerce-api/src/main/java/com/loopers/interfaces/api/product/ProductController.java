package com.loopers.interfaces.api.product;

import com.loopers.application.product.ProductCommand.GetProduct;
import com.loopers.application.product.ProductCriteria.ProductList;
import com.loopers.application.product.ProductFacade;
import com.loopers.application.product.ProductInfo;
import com.loopers.application.product.ProductInfo.ProductListInfo;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.product.ProductDto.V1.ProductListResponse;
import com.loopers.interfaces.api.product.ProductDto.V1.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/products")
@RestController
@RequiredArgsConstructor
public class ProductController implements ProductV1ApiSpec {
    private final ProductFacade productFacade;

    @Override
    @GetMapping("")
    public ApiResponse<ProductListResponse> getProductList(
            @RequestParam(defaultValue = "createdAt:desc") String sort,
            @PageableDefault(size = 10) Pageable pageable) {

        String[] sortParams = sort.split(":");
        SortField field = SortField.fromField(sortParams[0]);
        SortOrder order = SortOrder.valueOf(sortParams[1].toUpperCase());

        ProductListInfo productList = productFacade.getProductList(ProductList.from(
                field.getField(), order, pageable
        ));
        return ApiResponse.success(ProductListResponse.from(productList));
    }

    @Override
    @GetMapping("/{productId}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable Long productId) {
        GetProduct command = new GetProduct(productId);
        ProductInfo product = this.productFacade.getProduct(command);
        ProductResponse result = ProductResponse.from(product);
        return ApiResponse.success(result);
    }
}
