package io.minibuilds.prometheus.controller;

import io.minibuilds.prometheus.controller.request.CreateOrder;
import io.minibuilds.prometheus.controller.response.OrderDto;
import io.minibuilds.prometheus.domain.OrderService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/createOrder")
    public OrderDto createOrder(@RequestBody CreateOrder order) {
        return new OrderDto(orderService.createOrder(order.items()).id());
    }

    @PostMapping("/completeOrder/{id}")
    public void completeOrder(@PathVariable String id) {
        orderService.completeOrder(id);
    }

}
