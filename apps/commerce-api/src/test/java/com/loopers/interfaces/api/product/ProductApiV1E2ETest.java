package com.loopers.interfaces.api.product;

import static org.assertj.core.api.Assertions.assertThat;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.product.BrandId;
import com.loopers.domain.product.Product;
import com.loopers.domain.user.BirthDate;
import com.loopers.domain.user.Email;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.LoginId;
import com.loopers.domain.user.User;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.product.ProductDto.V1.ProductResponse;
import com.loopers.interfaces.api.product.ProductDto.V1.SearchProductResponse;
import com.loopers.utils.DatabaseCleanUp;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductApiV1E2ETest {

    public static final String END_POINT = "/api/v1/products";

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private DatabaseCleanUp databaseCleanUp;
    @Autowired
    private ProductJpaRepository productJpaRepository;
    @Autowired
    private BrandJpaRepository brandJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;

    private Brand brand;

    @BeforeEach
    void setUp() {
        brand = brandJpaRepository.save(new Brand("test brand"));
        userJpaRepository.save(new User(new LoginId("test"), new Email("test@test.com"), "test", new BirthDate("1998-01-08"), Gender.MALE));
    }

    @AfterEach
    public void cleanUp() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("상품 단건 조회 시,")
    @Nested
    class GetProduct {

        @DisplayName("성공할 경우, 상품 정보를 반환한다.")
        @Test
        void returnProductResponse_whenGetProductSuccessful() {
            // given
            Product product = productJpaRepository.save(new Product(new BrandId(brand.getId()), "test product", 10000L));

            // when
            ParameterizedTypeReference<ApiResponse<ProductResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<ProductResponse>> response = testRestTemplate.exchange(
                    END_POINT + "/" + product.getId(), HttpMethod.GET, null, responseType
            );

            // then
            Assertions.assertAll(
                    () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
                    () -> assertThat(response.getBody().meta().result()).isEqualTo(ApiResponse.Metadata.Result.SUCCESS),
                    () -> assertThat(response.getBody().data().id()).isEqualTo(product.getId())
            );
        }
    }

    @DisplayName("상품 목록 조회 시,")
    @Nested
    class SearchProducts {

        @BeforeEach
        void setUp() {
            List<Product> products = IntStream.range(0, 5).mapToObj(i ->
                    new Product(new BrandId(brand.getId()), "product " + i, 10000L * (i + 1))
            ).collect(Collectors.toList());
            productJpaRepository.saveAll(products);
        }

        @DisplayName("성공할 경우, 상품 목록을 반환한다.")
        @Test
        void returnProductList_whenSearchProductsSuccessful() {
            // when
            ParameterizedTypeReference<ApiResponse<SearchProductResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<SearchProductResponse>> response = testRestTemplate.exchange(
                    END_POINT, HttpMethod.GET, null, responseType
            );

            // then
            Assertions.assertAll(
                    () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
                    () -> assertThat(response.getBody().meta().result()).isEqualTo(ApiResponse.Metadata.Result.SUCCESS),
                    () -> assertThat(response.getBody().data().products()).hasSize(5)
            );
        }

        @DisplayName("최신순으로 정렬할 경우, ID 내림차순으로 정렬된 목록을 반환한다.")
        @Test
        void returnSortedList_whenSortByCreatedAt() {
            // when
            String url = END_POINT + "?sort=CREATED_AT&sortOrder=DESC";
            ParameterizedTypeReference<ApiResponse<SearchProductResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<SearchProductResponse>> response = testRestTemplate.exchange(url, HttpMethod.GET, null, responseType);

            // then
            List<Long> ids = response.getBody().data().products().stream().map(ProductResponse::id).collect(Collectors.toList());
            assertThat(ids).isSortedAccordingTo(Comparator.reverseOrder());
        }

        @DisplayName("가격 낮은 순으로 정렬할 경우, 가격 오름차순으로 정렬된 목록을 반환한다.")
        @Test
        void returnSortedList_whenSortByPrice() {
            // when
            String url = END_POINT + "?sort=PRICE&sortOrder=ASC";
            ParameterizedTypeReference<ApiResponse<SearchProductResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<SearchProductResponse>> response = testRestTemplate.exchange(url, HttpMethod.GET, null, responseType);

            // then
            List<Long> prices = response.getBody().data().products().stream().map(ProductResponse::price).collect(Collectors.toList());
            assertThat(prices).isSorted();
        }

        @DisplayName("페이징이 적용될 경우, 해당 페이지의 목록을 반환한다.")
        @Test
        void returnPagedList_whenPagingApplied() {
            // when
            String url = END_POINT + "?page=1&size=2";
            ParameterizedTypeReference<ApiResponse<SearchProductResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<ApiResponse<SearchProductResponse>> response = testRestTemplate.exchange(url, HttpMethod.GET, null, responseType);

            // then
            Assertions.assertAll(
                    () -> assertThat(response.getBody().data().products()).hasSize(2),
                    () -> assertThat(response.getBody().data().totalPages()).isEqualTo(3)
            );
        }
    }
}
