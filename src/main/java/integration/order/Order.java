package integration.order;

import car.domain.Car;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Order {
    private final UUID id;
    private final Car car;
    private final Instant createdAt;
    private final OrderStatus status;

    public Order(UUID id, Car car, Instant createdAt, OrderStatus status) {
        this.id = Objects.requireNonNull(id, "id");
        this.car = Objects.requireNonNull(car, "car");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        this.status = Objects.requireNonNull(status, "status");
    }

    public UUID getId() {
        return id;
    }

    public Car getCar() {
        return car;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
