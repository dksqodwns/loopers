package com.loopers.application.order;

import com.loopers.application.point.PointCommand;
import com.loopers.application.product.ProductCommand.DecreaseStock;
import com.loopers.application.product.ProductInfo;
import com.loopers.domain.order.OrderService;
import com.loopers.domain.point.PointService;
import com.loopers.domain.product.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderFacade {

    private final ProductService productService;
    private final OrderService orderService;
    private final PointService pointService;

    public OrderResult order(OrderCriteria.Order criteria) {
        List<ProductInfo> productInfoList = this.productService.getProductList(criteria.toGetProductListByIdCommand());
        OrderCommand.Order command = criteria.toCommand(productInfoList);
        OrderInfo orderInfo = orderService.place(command);
        pointService.use(new PointCommand.Use(criteria.userId(), orderInfo.totalPrice()));

        command.orderItemList().forEach(orderItem -> {
            productService.decreaseStock(new DecreaseStock(orderItem.productId(), orderItem.quantity()));
        });

        return OrderResult.from(orderInfo);
    }

}
