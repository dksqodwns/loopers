package com.loopers.domain.product;

import com.loopers.application.product.ProductCommand.Get;
import com.loopers.application.product.ProductInfo;
import com.loopers.application.product.ProductWithBrandInfo;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandRepository;
import com.loopers.domain.sort.SortManager;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;

    public Optional<ProductWithBrandInfo> getProductById(Get command) {
        Optional<Product> optionalProduct = this.productRepository.findByProductId(command.productId());
        Optional<Brand> optionalBrand = optionalProduct.flatMap(product1 ->
                brandRepository.findByBrandId(product1.getBrandId()));

        return optionalProduct.flatMap(product ->
                optionalBrand.map(brand -> ProductWithBrandInfo.from(product, brand)));
    }

    public Page<ProductInfo> getProductList(String sortBy, Pageable pageable) {
        Sort sort = SortManager.productSort(sortBy);
        Pageable query = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        return this.productRepository.findAll(query)
                .map(ProductInfo::from);
    }


}
