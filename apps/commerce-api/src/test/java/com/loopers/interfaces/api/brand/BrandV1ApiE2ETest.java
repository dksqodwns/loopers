package com.loopers.interfaces.api.brand;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loopers.domain.brand.Brand;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.ApiResponse.Metadata.Result;
import com.loopers.interfaces.api.brand.BrandDto.BrandResponse;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
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
public class BrandV1ApiE2ETest {

    public static final String END_POINT = "/api/v1/brands";
    private final TestRestTemplate testRestTemplate;
    private final DatabaseCleanUp databaseCleanUp;
    private final BrandJpaRepository brandJpaRepository;

    @Autowired
    public BrandV1ApiE2ETest(DatabaseCleanUp databaseCleanUp, TestRestTemplate testRestTemplate, BrandJpaRepository brandJpaRepository) {
        this.databaseCleanUp = databaseCleanUp;
        this.testRestTemplate = testRestTemplate;
        this.brandJpaRepository = brandJpaRepository;
    }

    @AfterEach
    public void cleanUp() {
        this.databaseCleanUp.truncateAllTables();
    }

    @DisplayName("GET /api/v1/brands/{brandId}")
    @Nested
    class GET {
        @DisplayName("브랜드 정보 조회에 성공 할 경우, 브랜드 정보를 반환한다.")
        @Test
        void returnBrandResponse_whenGetBrand() {
            Brand savedBrand = brandJpaRepository.save(Brand.create("카미엔"));

            ParameterizedTypeReference<ApiResponse<BrandResponse>> responseType = new ParameterizedTypeReference<>() {
            };

            ResponseEntity<ApiResponse<BrandResponse>> response = testRestTemplate.exchange(
                    END_POINT + "/" + savedBrand.getId(),
                    HttpMethod.GET,
                    new HttpEntity<>(savedBrand.getId()),
                    responseType
            );

            assertAll(
                    () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
                    () -> assertThat(response.getBody().meta().result()).isEqualTo(
                            Result.SUCCESS
                    )
            );
        }
    }
}
