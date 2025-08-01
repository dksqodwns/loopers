package com.loopers.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.loopers.application.order.OrderCommand;
import com.loopers.application.order.OrderInfo;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository mockOrderRepository;

    @DisplayName("주문 생성 시,")
    @Nested
    class PlaceOrder {

        @DisplayName("정상적인 요청이면, 주문이 저장된다.")
        @Test
        void saveOrder_whenValidRequest() {
            // given
            String userId = "testUser";
            List<OrderCommand.Order.OrderItem> orderItems = List.of(
                new OrderCommand.Order.OrderItem(1L, 2, 10000)
            );
            OrderCommand.Order command = new OrderCommand.Order(userId, orderItems);

            given(mockOrderRepository.save(any(Order.class))).willAnswer(invocation -> invocation.getArgument(0));

            // when
            OrderInfo orderInfo = orderService.place(command);

            // then
            verify(mockOrderRepository, times(1)).save(any(Order.class));
            assertThat(orderInfo.userId()).isEqualTo(userId);
            assertThat(orderInfo.orderStatus()).isEqualTo(OrderStatus.PENDING);
            assertThat(orderInfo.totalPrice()).isEqualTo(20000);
        }
    }
}
