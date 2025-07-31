package com.loopers.interfaces.api.product;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.loopers.domain.product.Product;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.product.ProductDto.V1.ProductResponse;
import com.loopers.utils.DatabaseCleanUp;
import java.util.List;
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

    @Autowired
    public ProductV1ApiE2ETest(TestRestTemplate testRestTemplate, DatabaseCleanUp databaseCleanUp, ProductJpaRepository productJpaRepository) {
        this.testRestTemplate = testRestTemplate;
        this.databaseCleanUp = databaseCleanUp;
        this.productJpaRepository = productJpaRepository;
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
            Product product = new Product(
                    "테스트 상품1", 1_000, 10, 1L
            );
            Product savedProduct = productJpaRepository.save(product);

            ParameterizedTypeReference<ApiResponse<List<ProductResponse>>> responseType = new ParameterizedTypeReference<>() {
            };

            ResponseEntity<ApiResponse<List<ProductResponse>>> response = testRestTemplate.exchange(
                    END_POINT,
                    HttpMethod.GET,
                    new HttpEntity<>(null, null),
                    responseType
            );
            Assertions.assertAll(
                    () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
                    () -> assertThat(response.getBody().data().get(0).id()).isEqualTo(savedProduct.getId())
            );
        }

        @DisplayName("상품 상세 조회 시, 상품에 대한 상세 정보가 반환된다.")
        @Test
        void returnProduct_whenGetProductByProductId() {
            Product product = new Product(
                    "테스트 상품1", 1_000, 10, 1L
            );
            Product savedProduct = productJpaRepository.save(product);

            ParameterizedTypeReference<ApiResponse<ProductResponse>> responseType = new ParameterizedTypeReference<>() {
            };

            ResponseEntity<ApiResponse<ProductResponse>> response = testRestTemplate.exchange(
                    END_POINT + "/" + 1L,
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
