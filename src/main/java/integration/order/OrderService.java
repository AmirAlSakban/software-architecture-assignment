package integration.order;

import car.domain.Car;

import java.time.Instant;
import java.util.UUID;

public class OrderService {

    public Order placeOrder(Car car) {
        return new Order(UUID.randomUUID(), car, Instant.now(), OrderStatus.PLACED);
    }
}
