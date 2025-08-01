package com.loopers.interfaces.api.order;

import com.loopers.application.order.OrderFacade;
import com.loopers.application.order.OrderResult;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.order.OrderDto.V1.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@RestController
public class OrderController {
    private final OrderFacade orderFacade;

    @PostMapping("")
    public ApiResponse<OrderDto.V1.OrderResponse> order(
            @RequestHeader("X-USER-ID") String userId,
            @RequestBody OrderDto.V1.OrderRequest request
    ) {
        OrderResult orderResult = this.orderFacade.order(request.toCriteria(userId));
        OrderResponse response = OrderResponse.from(orderResult);
        return ApiResponse.success(response);
    }

}
