package com.loopers.interfaces.api.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loopers.application.user.UserCommand;
import com.loopers.domain.brand.Brand;
import com.loopers.domain.point.Point;
import com.loopers.domain.product.Product;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.User;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.point.PointJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.order.OrderDto.V1.OrderItemRequest;
import com.loopers.interfaces.api.order.OrderDto.V1.OrderRequest;
import com.loopers.interfaces.api.order.OrderDto.V1.OrderResponse;
import com.loopers.utils.DatabaseCleanUp;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderV1ApiE2ETest {

    public static final String END_POINT = "/api/v1/orders";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private BrandJpaRepository brandJpaRepository;

    @Autowired
    private PointJpaRepository pointJpaRepository;

    @AfterEach
    public void cleanUp() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("POST /api/v1/orders")
    @Nested
    class Order {

        @DisplayName("정상적인 주문 요청 시, 주문 정보를 반환한다.")
        @Test
        void returnOrderResponse_whenOrderPlaced() {
            // given
            User user = User.create(new UserCommand.UserCreateCommand("testUser", "테스터", "test@test.com", Gender.MALE, LocalDate.now().minusYears(20)));
            userJpaRepository.save(user);

            Brand brand = Brand.create("testBrand");
            brandJpaRepository.save(brand);

            Product product1 = new Product("testProduct1", 10000, 10, brand.getId());
            Product product2 = new Product("testProduct2", 5000, 5, brand.getId());

            Product savedProduct1 = productJpaRepository.save(product1);
            Product savedProduct2 = productJpaRepository.save(product2);

            Point point = Point.create(user.getUserId());
            point.charge(50000);
            pointJpaRepository.save(point);

            List<OrderItemRequest> orderItems = List.of(
                    new OrderItemRequest(savedProduct1.getId(), 2),
                    new OrderItemRequest(savedProduct2.getId(), 3)
            );

            OrderRequest request = new OrderRequest(orderItems);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("X-USER-ID", user.getUserId());
            HttpEntity<OrderRequest> requestEntity = new HttpEntity<>(request, headers);

            ParameterizedTypeReference<ApiResponse<OrderResponse>> responseType = new ParameterizedTypeReference<>() {
            };

            // when
            ResponseEntity<ApiResponse<OrderResponse>> response = testRestTemplate.exchange(
                    END_POINT,
                    HttpMethod.POST,
                    requestEntity,
                    responseType
            );
            System.out.println("리스폰스: " + response);
            // then
            assertAll(
                    () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
                    () -> assertThat(response.getBody().meta().result()).isEqualTo(ApiResponse.Metadata.Result.SUCCESS),
                    () -> assertThat(response.getBody().data().userId()).isEqualTo(user.getUserId()),
                    () -> assertThat(response.getBody().data().totalPrice()).isEqualTo(35000)
            );

            Product updatedProduct1 = productJpaRepository.findById(savedProduct1.getId()).get();
            Product updatedProduct2 = productJpaRepository.findById(savedProduct2.getId()).get();
            Point updatedPoint = pointJpaRepository.findByUserId(user.getUserId()).get();

            assertAll(
                    () -> assertThat(updatedProduct1.getStock()).isEqualTo(8),
                    () -> assertThat(updatedProduct2.getStock()).isEqualTo(2),
                    () -> assertThat(updatedPoint.getPoint()).isEqualTo(15000)
            );
        }
    }
}
