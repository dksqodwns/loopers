package com.loopers.application.order;

import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderStatus;

public record OrderInfo(
        Long id,
        String userId,
        OrderStatus orderStatus,
        Integer totalPrice
) {

    public static OrderInfo from(Order order) {
        return new OrderInfo(
                order.getId(),
                order.getUserId(),
                order.getOrderStatus(),
                order.calculateTotalPrice()
        );
    }


}
