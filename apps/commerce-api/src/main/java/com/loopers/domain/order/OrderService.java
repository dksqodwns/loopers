package com.loopers.domain.order;

import com.loopers.application.order.OrderCommand;
import com.loopers.application.order.OrderInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Transactional
    public OrderInfo place(OrderCommand.Order command) {
        List<OrderItem> orderItemList = command.orderItemList().stream()
                .map(orderItem -> new OrderItem(
                        orderItem.productId(),
                        orderItem.quantity(),
                        orderItem.price()
                ))
                .toList();
        Order order = new Order(command.userId(), orderItemList);
        return OrderInfo.from(orderRepository.save(order));
    }

}
