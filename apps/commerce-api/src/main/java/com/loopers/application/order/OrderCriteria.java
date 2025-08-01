package com.loopers.application.order;

import com.loopers.application.product.ProductCommand;
import com.loopers.application.product.ProductInfo;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record OrderCriteria() {

    public record Order(String userId, List<OrderCriteria.Order.OrderItem> orderItemList) {

        public OrderCommand.Order toCommand(List<ProductInfo> productInfoList) {

            Map<Long, Integer> productQuantities = orderItemList.stream()
                    .collect(Collectors.toMap(
                            OrderCriteria.Order.OrderItem::productId,
                            OrderCriteria.Order.OrderItem::quantity
                    ));

            List<OrderCommand.Order.OrderItem> orderItmeList = productInfoList.stream()
                    .map(productInfo -> new OrderCommand.Order.OrderItem(
                            productInfo.id(),
                            productQuantities.get(productInfo.id()),
                            productInfo.price()
                    ))
                    .toList();

            return new OrderCommand.Order(userId, orderItmeList);
        }

        public ProductCommand.GetProductList toGetProductListCommand() {
            return new ProductCommand.GetProductList(getProductIdList());
        }

        private List<Long> getProductIdList() {
            return orderItemList.stream()
                    .map(orderItme -> orderItme.productId)
                    .toList();
        }

        public record OrderItem(Long productId, Integer quantity) {
        }

    }
}
