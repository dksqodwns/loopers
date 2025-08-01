package com.loopers.application.order;

import com.loopers.domain.order.OrderStatus;

public record OrderResult(
        Long id,
        String userId,
        OrderStatus orderStatus,
        Integer totalPrice
) {
    public static OrderResult from(OrderInfo orderInfo) {
        return new OrderResult(
                orderInfo.id(),
                orderInfo.userId(),
                orderInfo.orderStatus(),
                orderInfo.totalPrice()
        );
    }
}
