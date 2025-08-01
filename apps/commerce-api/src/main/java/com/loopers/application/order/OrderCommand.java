package com.loopers.application.order;

import java.util.List;

public record OrderCommand() {
    public record Order(String userId, List<OrderItem> orderItemList) {
        public record OrderItem(Long productId, Integer quantity, Integer price) {}
    }
}
