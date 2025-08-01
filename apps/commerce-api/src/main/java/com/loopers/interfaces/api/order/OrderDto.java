package com.loopers.interfaces.api.order;

import com.loopers.application.order.OrderCriteria;
import com.loopers.application.order.OrderCriteria.Order.OrderItem;
import com.loopers.application.order.OrderResult;
import java.util.List;

public class OrderDto {
    public record V1() {

        public record OrderResponse(Long id, String userId, String orderStatus, Integer totalPrice) {
            public static OrderResponse from(OrderResult orderResult) {
                return new OrderResponse(
                        orderResult.id(),
                        orderResult.userId(),
                        orderResult.orderStatus().name(),
                        orderResult.totalPrice()
                );
            }
        }

        public record OrderRequest(
                List<OrderItemRequest> orderItemList
        ) {
            public OrderCriteria.Order toCriteria(String userId) {
                List<OrderItem> orderItemList = this.orderItemList.stream()
                        .map(OrderItemRequest::toCreiteria)
                        .toList();

                return new OrderCriteria.Order(userId, orderItemList);
            }

        }

        record OrderItemRequest(Long productId, Integer quantity) {

            private OrderCriteria.Order.OrderItem toCreiteria() {
                return new OrderCriteria.Order.OrderItem(productId, quantity);
            }

        }
    }

}
