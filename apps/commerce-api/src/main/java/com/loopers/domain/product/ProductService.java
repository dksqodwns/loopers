package com.loopers.domain.product;

import com.loopers.application.product.ProductCommand;
import com.loopers.application.product.ProductCommand.GetProduct;
import com.loopers.application.product.ProductInfo;
import com.loopers.application.product.ProductWithBrandInfo;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;

    public Optional<ProductWithBrandInfo> getProductById(GetProduct command) {
        Optional<Product> optionalProduct = this.productRepository.findByProductId(command.productId());
        Optional<Brand> optionalBrand = optionalProduct.flatMap(product1 ->
                brandRepository.findByBrandId(product1.getBrandId()));

        return optionalProduct.flatMap(product ->
                optionalBrand.map(brand -> ProductWithBrandInfo.from(product, brand)));
    }

//    public Page<ProductInfo> getProductList(String sortBy, Pageable pageable) {
//        Sort sort = SortManager.productSort(sortBy);
//        Pageable query = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
//
//        return this.productRepository.findAll(query)
//                .map(ProductInfo::from);
//    }

    @Transactional(readOnly = true)
    public List<ProductInfo> getProductList(ProductCommand.GetProductList command) {
        return this.productRepository.findAllByIdList(command.productIdList()).stream()
                .map(ProductInfo::from)
                .toList();
    }

    public Product decreaseStock(ProductCommand.DecreaseStock command) {
        Product product = productRepository.findByProductId(command.productId())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND,
                        "상품을 찾을 수 없습니다. productId: " + command.productId()));
        Product updatedProduct = product.decreaseStock(command.quantity());
        return productRepository.save(updatedProduct);
    }






}
