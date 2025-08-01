package com.loopers.interfaces.api.product;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.loopers.application.like.LikeCommand;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.like.Like;
import com.loopers.domain.product.Product;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.like.LikeJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.product.ProductDto.V1.ProductListResponse;
import com.loopers.interfaces.api.product.ProductDto.V1.ProductResponse;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductV1ApiE2ETest {

    public static final String END_POINT = "/api/v1/products";
    private final TestRestTemplate testRestTemplate;
    private final DatabaseCleanUp databaseCleanUp;
    private final ProductJpaRepository productJpaRepository;
    private final BrandJpaRepository brandJpaRepository;
    private final LikeJpaRepository likeJpaRepository;

    @Autowired
    public ProductV1ApiE2ETest(
            TestRestTemplate testRestTemplate,
            DatabaseCleanUp databaseCleanUp,
            ProductJpaRepository productJpaRepository,
            BrandJpaRepository brandJpaRepository,
            LikeJpaRepository likeJpaRepository
    ) {
        this.testRestTemplate = testRestTemplate;
        this.databaseCleanUp = databaseCleanUp;
        this.productJpaRepository = productJpaRepository;
        this.brandJpaRepository = brandJpaRepository;
        this.likeJpaRepository = likeJpaRepository;
    }

    @AfterEach
    public void cleanUp() {
        this.databaseCleanUp.truncateAllTables();
    }

    @DisplayName("GET /api/v1/products, /api/v1/products/{productId}")
    @Nested
    class GET {
        @DisplayName("상품 전체 조회 시, 상품 목록이 반환된다.")
        @Test
        void returnProductList_whenGetProducts() {
            Brand brand = brandJpaRepository.save(Brand.create("테스트 브랜드"));

            Product product1 = new Product(
                    "테스트 상품1", 1_000, 10, 1L
            );
            Product product2 = new Product(
                    "테스트 상품2", 10_000, 10, 1L
            );
            Product product3 = new Product(
                    "테스트 상품3", 5_000, 10, 1L
            );
            productJpaRepository.save(product1);
            productJpaRepository.save(product2);
            productJpaRepository.save(product3);

            ParameterizedTypeReference<ApiResponse<ProductListResponse>> responseType = new ParameterizedTypeReference<>() {
            };

            ResponseEntity<ApiResponse<ProductListResponse>> response = testRestTemplate.exchange(
                    END_POINT,
                    HttpMethod.GET,
                    new HttpEntity<>(null, null),
                    responseType
            );

            Assertions.assertAll(
                    () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue()
            );
        }

        @DisplayName("상품이 10개 이상일 때 페이징이 올바르게 동작한다.")
        @Test
        void testPaginationWhenMoreThan10Products() {
            Brand brand = brandJpaRepository.save(Brand.create("테스트 브랜드"));

            for (int i = 0; i < 15; i++) {
                productJpaRepository.save(new Product("상품" + i, 1000 + i, 10, brand.getId()));
            }

            ParameterizedTypeReference<ApiResponse<ProductListResponse>> responseType = new ParameterizedTypeReference<>() {
            };

            ResponseEntity<ApiResponse<ProductListResponse>> response1 = testRestTemplate.exchange(
                    END_POINT + "?size=5",
                    HttpMethod.GET,
                    new HttpEntity<>(null, null),
                    responseType
            );

            System.out.println("리스폰스: "+response1);
            Assertions.assertAll(
                    () -> assertThat(response1.getStatusCode().is2xxSuccessful()).isTrue(),
                    () -> assertThat(response1.getBody().data().productList().size()).isEqualTo(5)
            );

            ResponseEntity<ApiResponse<ProductListResponse>> response2 = testRestTemplate.exchange(
                    END_POINT + "?page=1&size=5",
                    HttpMethod.GET,
                    new HttpEntity<>(null, null),
                    responseType
            );

            System.out.println("리스폰스: "+response2);

            Assertions.assertAll(
                    () -> assertThat(response2.getStatusCode().is2xxSuccessful()).isTrue(),
                    () -> assertThat(response2.getBody().data().productList().size()).isEqualTo(5)
            );
        }

        @DisplayName("좋아요가 많은 순으로 상품 목록이 정렬된다.")
        @Test
        void testSortByLikeCountDesc() {
            Brand brand = brandJpaRepository.save(Brand.create("테스트 브랜드"));

            Product product1 = productJpaRepository.save(new Product("상품1", 1000, 10, brand.getId()));
            Product product2 = productJpaRepository.save(new Product("상품2", 2000, 10, brand.getId()));
            Product product3 = productJpaRepository.save(new Product("상품3", 3000, 10, brand.getId()));

            // 좋아요 데이터 생성
            likeJpaRepository.save(Like.create(new LikeCommand.Like("유저1", product3.getId())));
            likeJpaRepository.save(Like.create(new LikeCommand.Like("유저2", product3.getId())));
            likeJpaRepository.save(Like.create(new LikeCommand.Like("유저3", product3.getId())));
            likeJpaRepository.save(Like.create(new LikeCommand.Like("유저4", product3.getId())));
            likeJpaRepository.save(Like.create(new LikeCommand.Like("유저5", product3.getId())));
            likeJpaRepository.save(Like.create(new LikeCommand.Like("유저1", product1.getId())));
            likeJpaRepository.save(Like.create(new LikeCommand.Like("유저2", product1.getId())));
            likeJpaRepository.save(Like.create(new LikeCommand.Like("유저3", product1.getId())));

            ParameterizedTypeReference<ApiResponse<ProductListResponse>> responseType = new ParameterizedTypeReference<>() {
            };

            ResponseEntity<ApiResponse<ProductListResponse>> response = testRestTemplate.exchange(
                    END_POINT + "?sort=likeCount:desc",
                    HttpMethod.GET,
                    new HttpEntity<>(null, null),
                    responseType
            );

            System.out.println("HTTP Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());

            Assertions.assertAll(
                    () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
                    () -> assertThat(response.getBody().data().productList().get(0).name()).isEqualTo("상품3"),
                    () -> assertThat(response.getBody().data().productList().get(1).name()).isEqualTo("상품1"),
                    () -> assertThat(response.getBody().data().productList().get(2).name()).isEqualTo("상품2")
            );
        }

        @DisplayName("가격이 낮은 순으로 상품 목록이 정렬된다.")
        @Test
        void testSortByPriceAsc() {
            Brand brand = brandJpaRepository.save(Brand.create("테스트 브랜드"));
            productJpaRepository.save(new Product("상품1", 3000, 10, brand.getId()));
            productJpaRepository.save(new Product("상품2", 1000, 10, brand.getId()));
            productJpaRepository.save(new Product("상품3", 2000, 10, brand.getId()));

            ParameterizedTypeReference<ApiResponse<ProductListResponse>> responseType = new ParameterizedTypeReference<>() {
            };

            ResponseEntity<ApiResponse<ProductListResponse>> response = testRestTemplate.exchange(
                    END_POINT + "?sort=price:asc",
                    HttpMethod.GET,
                    new HttpEntity<>(null, null),
                    responseType
            );

            Assertions.assertAll(
                    () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
                    () -> assertThat(response.getBody().data().productList().get(0).name()).isEqualTo("상품2"),
                    () -> assertThat(response.getBody().data().productList().get(1).name()).isEqualTo("상품3"),
                    () -> assertThat(response.getBody().data().productList().get(2).name()).isEqualTo("상품1")
            );
        }

        @DisplayName("최신순으로 상품 목록이 정렬된다.")
        @Test
        void testSortByCreatedAtDesc() {
            Brand brand = brandJpaRepository.save(Brand.create("테스트 브랜드"));

            productJpaRepository.save(new Product("상품1", 1000, 10, brand.getId()));
            productJpaRepository.save(new Product("상품2", 2000, 10, brand.getId()));
            productJpaRepository.save(new Product("상품3", 3000, 10, brand.getId()));

            ParameterizedTypeReference<ApiResponse<ProductListResponse>> responseType = new ParameterizedTypeReference<>() {
            };

            ResponseEntity<ApiResponse<ProductListResponse>> response = testRestTemplate.exchange(
                    END_POINT + "?sort=createdAt:desc",
                    HttpMethod.GET,
                    new HttpEntity<>(null, null),
                    responseType
            );

            Assertions.assertAll(
                    () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
                    () -> assertThat(response.getBody().data().productList().get(0).name()).isEqualTo("상품3"),
                    () -> assertThat(response.getBody().data().productList().get(1).name()).isEqualTo("상품2"),
                    () -> assertThat(response.getBody().data().productList().get(2).name()).isEqualTo("상품1")
            );
        }


        @DisplayName("상품 상세 조회 시, 상품에 대한 상세 정보가 반환된다.")
        @Test
        void returnProduct_whenGetProductByProductId() {
            Product product = new Product(
                    "테스트 상품1", 1_000, 10, 1L
            );
            Brand brand = brandJpaRepository.save(Brand.create("테스트 브랜드"));

            Product savedProduct = productJpaRepository.save(product);
            brandJpaRepository.save(brand);
            ParameterizedTypeReference<ApiResponse<ProductResponse>> responseType = new ParameterizedTypeReference<>() {
            };

            ResponseEntity<ApiResponse<ProductResponse>> response = testRestTemplate.exchange(
                    END_POINT + "/" + 1,
                    HttpMethod.GET,
                    new HttpEntity<>(null, null),
                    responseType
            );

            Assertions.assertAll(
                    () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
                    () -> assertThat(response.getBody().data().id()).isEqualTo(savedProduct.getId())
            );

        }
    }

}
