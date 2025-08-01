package com.loopers.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

class OrderTest {

    @DisplayName("주문 생성 시,")
    @Nested
    class CreateOrder {

        @DisplayName("유저 ID가 null이면, 400 Bad Request를 반환한다.")
        @Test
        void badRequest_whenNullUserId() {
            // given
            List<OrderItem> orderItems = List.of(new OrderItem(1L, 1, 1000));

            // when
            CoreException coreException = assertThrows(CoreException.class, () -> new Order(null, orderItems));

            // then
            assertThat(coreException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("주문 상품이 비어있으면, 400 Bad Request를 반환한다.")
        @ParameterizedTest
        @NullSource
        void badRequest_whenEmptyOrderItems(List<OrderItem> orderItems) {
            // given
            String userId = "testUser";

            // when
            CoreException coreException = assertThrows(CoreException.class, () -> new Order(userId, orderItems));

            // then
            assertThat(coreException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("주문 상품이 없으면, 400 Bad Request를 반환한다.")
        @Test
        void badRequest_whenNoOrderItems() {
            // given
            String userId = "testUser";
            List<OrderItem> orderItems = Collections.emptyList();

            // when
            CoreException coreException = assertThrows(CoreException.class, () -> new Order(userId, orderItems));

            // then
            assertThat(coreException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("정상적인 요청이면, 주문이 생성된다.")
        @Test
        void createOrder_whenValidRequest() {
            // given
            String userId = "testUser";
            List<OrderItem> orderItems = List.of(new OrderItem(1L, 2, 10000));

            // when
            Order order = new Order(userId, orderItems);

            // then
            assertThat(order.getUserId()).isEqualTo(userId);
            assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PENDING);
            assertThat(order.getOrderItems()).hasSize(1);
        }
    }

    @DisplayName("주문 상품 생성 시,")
    @Nested
    class CreateOrderItem {

        @DisplayName("상품 ID가 null이면, 400 Bad Request를 반환한다.")
        @Test
        void badRequest_whenNullProductId() {
            // when
            CoreException coreException = assertThrows(CoreException.class, () -> new OrderItem(null, 1, 1000));

            // then
            assertThat(coreException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("수량이 null이면, 400 Bad Request를 반환한다.")
        @Test
        void badRequest_whenNullQuantity() {
            // when
            CoreException coreException = assertThrows(CoreException.class, () -> new OrderItem(1L, null, 1000));

            // then
            assertThat(coreException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("수량이 1보다 작으면, 400 Bad Request를 반환한다.")
        @Test
        void badRequest_whenQuantityIsLessThanOne() {
            // when
            CoreException coreException = assertThrows(CoreException.class, () -> new OrderItem(1L, 0, 1000));

            // then
            assertThat(coreException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("가격이 null이면, 400 Bad Request를 반환한다.")
        @Test
        void badRequest_whenNullPrice() {
            // when
            CoreException coreException = assertThrows(CoreException.class, () -> new OrderItem(1L, 1, null));

            // then
            assertThat(coreException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("가격이 0보다 작으면, 400 Bad Request를 반환한다.")
        @Test
        void badRequest_whenPriceIsLessThanZero() {
            // when
            CoreException coreException = assertThrows(CoreException.class, () -> new OrderItem(1L, 1, -100));

            // then
            assertThat(coreException.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }
    }

    @DisplayName("총 주문 금액 계산 시,")
    @Nested
    class CalculateTotalPrice {

        @DisplayName("정확한 총액이 계산된다.")
        @Test
        void calculateTotalPrice() {
            // given
            String userId = "testUser";
            OrderItem item1 = new OrderItem(1L, 2, 10000);
            OrderItem item2 = new OrderItem(2L, 3, 5000);
            List<OrderItem> orderItems = List.of(item1, item2);
            Order order = new Order(userId, orderItems);

            // when
            Integer totalPrice = order.calculateTotalPrice();

            // then
            assertThat(totalPrice).isEqualTo(35000);
        }
    }
}
