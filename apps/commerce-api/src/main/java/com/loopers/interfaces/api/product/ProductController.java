package com.loopers.interfaces.api.product;

import com.loopers.application.product.ProductCommand;
import com.loopers.application.product.ProductFacade;
import com.loopers.application.product.ProductWithBrandInfo;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.product.ProductDto.V1.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/products")
@RestController
@RequiredArgsConstructor
public class ProductController implements ProductV1ApiSpec {
    private final ProductFacade productFacade;

    @Override
    @GetMapping("/{productId}")
    public ApiResponse<ProductDto.V1.ProductResponse> getProductById(@PathVariable Long productId) {
        ProductCommand.Get command = new ProductCommand.Get(productId);
        ProductWithBrandInfo product = this.productFacade.getProduct(command);
        ProductResponse result = ProductResponse.from(product);
        return ApiResponse.success(result);
    }
}

//    @Override
//    @GetMapping("")
//    public ApiResponse<Page<ProductResponse>> getProductList(@RequestParam(defaultValue = "latest") String sortBy,
//                                                             @PageableDefault(size = 10) Pageable pageable) {
//
//        Page<ProductInfo> productList = this.productFacade.getProductList(sortBy, pageable);
//        Page<ProductResponse> result = productList.map(ProductResponse::from);
//        return ApiResponse.success(result);
//    }
