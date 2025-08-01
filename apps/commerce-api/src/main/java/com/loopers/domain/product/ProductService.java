package com.loopers.domain.product;

import com.loopers.application.product.ProductCommand;
import com.loopers.application.product.ProductCommand.GetProduct;
import com.loopers.application.product.ProductCommand.GetProductListById;
import com.loopers.application.product.ProductInfo;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandRepository;
import com.loopers.domain.like.LikeRepository;
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
    private final BrandRepository brandRepository;
    private final LikeRepository likeRepository;


    @Transactional(readOnly = true)
    public Optional<ProductInfo> getProductById(GetProduct command) {
        Optional<Product> optionalProduct = this.productRepository.findByProductId(command.productId());
        Optional<Brand> optionalBrand = optionalProduct.flatMap(product1 ->
                brandRepository.findByBrandId(product1.getBrandId()));
        Long likeCount = likeRepository.countByProductId(command.productId());
        return optionalProduct.flatMap(product ->
                optionalBrand.map(brand -> ProductInfo.from(product, brand, likeCount)));
    }


    @Transactional(readOnly = true)
    public List<ProductInfo> getProductList(GetProductListById command) {
        return this.productRepository.findAllByIdList(command.productIdList()).stream()
                .map(ProductInfo::from)
                .toList();
    }

    public Page<ProductInfo> getProductList(ProductCommand.GetProductList command) {
        boolean sortByLikeCount = command.pageRequest().getSort().stream()
                .anyMatch(order -> order.getProperty().equals("likeCount"));

        if (sortByLikeCount) {
            return productRepository.findAllWithLikeCount(command.pageRequest()).map(productWithLikeCount -> {
                Product product = productWithLikeCount.getProduct();
                Brand brand = brandRepository.findByBrandId(product.getBrandId())
                        .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND,
                                "브랜드를 찾을 수 없습니다. brandId: " + product.getBrandId()));
                return ProductInfo.from(product, brand, productWithLikeCount.getLikeCount());
            });
        } else {
            return productRepository.findAll(command.pageRequest()).map(product -> {
                Brand brand = brandRepository.findByBrandId(product.getBrandId())
                        .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND,
                                "브랜드를 찾을 수 없습니다. brandId: " + product.getBrandId()));
                Long likeCount = likeRepository.countByProductId(product.getId());
                return ProductInfo.from(product, brand, likeCount);
            });
        }
    }


    @Transactional()
    public void decreaseStock(ProductCommand.DecreaseStock command) {
        Product product = productRepository.findByProductId(command.productId())
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND,
                        "상품을 찾을 수 없습니다. productId: " + command.productId()));
        Product updatedProduct = product.decreaseStock(command.quantity());
        productRepository.save(updatedProduct);
    }
}
