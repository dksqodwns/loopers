package com.loopers.domain.product;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Nested
    @DisplayName("getProduct 메서드는")
    class GetProduct {

        @Test
        @DisplayName("존재하는 상품 ID로 조회하면 상품 정보를 반환한다")
        void getProduct_withExistingId_returnsProductInfo() {
            // given
            Long productId = 1L;
            Product product = new Product(new BrandId(1L), "Test Product", 10000L);
            given(productRepository.findById(productId)).willReturn(Optional.of(product));

            // when
            ProductInfo productInfo = productService.getProduct(new ProductCommand.GetProduct(productId));

            // then
            assertThat(productInfo).isNotNull();
            assertThat(productInfo.name()).isEqualTo("Test Product");
        }

        @Test
        @DisplayName("존재하지 않는 상품 ID로 조회하면 CoreException을 던진다")
        void getProduct_withNonExistingId_throwsCoreException() {
            // given
            Long productId = 1L;
            given(productRepository.findById(productId)).willReturn(Optional.empty());

            // when & then
            CoreException exception = assertThrows(CoreException.class, () -> {
                productService.getProduct(new ProductCommand.GetProduct(productId));
            });
            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("getProductById 메서드는")
    class GetProductById {

        @Test
        @DisplayName("존재하는 상품 ID로 조회하면 Optional<ProductInfo>를 반환한다")
        void getProductById_withExistingId_returnsOptionalProductInfo() {
            // given
            Long productId = 1L;
            Product product = new Product(new BrandId(1L), "Test Product", 10000L);
            given(productRepository.findById(productId)).willReturn(Optional.of(product));

            // when
            Optional<ProductInfo> productInfo = productService.getProductById(new ProductCommand.GetProduct(productId));

            // then
            assertThat(productInfo).isPresent();
            assertThat(productInfo.get().name()).isEqualTo("Test Product");
        }

        @Test
        @DisplayName("존재하지 않는 상품 ID로 조회하면 빈 Optional을 반환한다")
        void getProductById_withNonExistingId_returnsEmptyOptional() {
            // given
            Long productId = 1L;
            given(productRepository.findById(productId)).willReturn(Optional.empty());

            // when
            Optional<ProductInfo> productInfo = productService.getProductById(new ProductCommand.GetProduct(productId));

            // then
            assertThat(productInfo).isEmpty();
        }
    }

    @Nested
    @DisplayName("searchProducts 메서드는")
    class SearchProducts {

        @Test
        @DisplayName("브랜드 ID가 주어지면 해당 브랜드의 상품 목록을 페이지로 반환한다")
        void searchProducts_withBrandId_returnsPagedProductInfo() {
            // given
            Long brandId = 1L;
            PageRequest pageRequest = PageRequest.of(0, 10);
            List<Product> products = Collections.singletonList(new Product(new BrandId(brandId), "Test Product", 10000L));
            Page<Product> pagedProducts = new PageImpl<>(products, pageRequest, products.size());
            given(productRepository.findAllBy(brandId, pageRequest)).willReturn(pagedProducts);

            // when
            Page<ProductInfo> result = productService.searchProducts(new ProductCommand.SerachProducts(brandId, pageRequest));

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).getBrandId()).isEqualTo(brandId);
        }

        @Test
        @DisplayName("브랜드 ID가 없으면 모든 상품 목록을 페이지로 반환한다")
        void searchProducts_withoutBrandId_returnsPagedProductInfo() {
            // given
            PageRequest pageRequest = PageRequest.of(0, 10);
            List<Product> products = Collections.singletonList(new Product(new BrandId(1L), "Test Product", 10000L));
            Page<Product> pagedProducts = new PageImpl<>(products, pageRequest, products.size());
            given(productRepository.findAllBy(pageRequest)).willReturn(pagedProducts);

            // when
            Page<ProductInfo> result = productService.searchProducts(new ProductCommand.SerachProducts(null, pageRequest));

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("getProducts 메서드는")
    class GetProducts {

        @Test
        @DisplayName("상품 ID 목록으로 조회하면 상품 정보 목록을 반환한다")
        void getProducts_withIds_returnsListOfProductInfo() {
            // given
            List<Long> ids = List.of(1L, 2L);
            List<Product> products = List.of(
                    new Product(new BrandId(1L), "Product 1", 10000L),
                    new Product(new BrandId(1L), "Product 2", 20000L)
            );
            given(productRepository.findAllByIds(ids)).willReturn(products);

            // when
            List<ProductInfo> result = productService.getProducts(new ProductCommand.GetProducts(ids));

            // then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).name()).isEqualTo("Product 1");
            assertThat(result.get(1).name()).isEqualTo("Product 2");
        }
    }
}