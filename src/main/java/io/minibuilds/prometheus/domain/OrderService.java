package io.minibuilds.prometheus.domain;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.minibuilds.prometheus.domain.model.Order;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    List<Order> orders = Collections.synchronizedList(new ArrayList<>());

    private final Counter totalOrders;
    private final AtomicInteger activeOrders;
    private final DistributionSummary orderCompletion;

    public OrderService(MeterRegistry meterRegistry) {
        totalOrders = meterRegistry.counter("no.of.orders");
        activeOrders = meterRegistry.gauge("no.of.active.orders", new AtomicInteger(0));
        orderCompletion = DistributionSummary.builder("order.completion.seconds")
                .publishPercentileHistogram()
                .publishPercentiles(0.05, 0.5, 0.95)
                .register(meterRegistry);
    }

    public Order createOrder(List<String> items) {
        Order order = new Order(UUID.randomUUID().toString(), items, LocalDateTime.now());
        orders.add(order);

        totalOrders.increment();
        activeOrders.set(orders.size());

        return order;
    }

    public void completeOrder(String id) {
        Order order = orders.stream()
                .filter(o -> o.id().equals(id))
                .findFirst()
                .orElseThrow();

        orders.remove(order);

        Duration orderDuration = Duration.between(order.createdDateTime(), LocalDateTime.now());
        orderCompletion.record(orderDuration.getSeconds());

        activeOrders.set(orders.size());
    }
}
