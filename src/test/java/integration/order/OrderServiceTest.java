package integration.order;

import car.builder.CarBuilder;
import car.domain.CarModel;
import car.domain.EngineType;
import car.domain.TransmissionType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    @Test
    void placeOrderShouldReturnPlacedOrder() {
        var car = new CarBuilder()
                .withModel(CarModel.SUV)
                .withEngine(EngineType.V6)
                .withTransmission(TransmissionType.AUTOMATIC)
                .build();

        OrderService service = new OrderService();
        Order order = service.placeOrder(car);

        assertNotNull(order.getId());
        assertEquals(OrderStatus.PLACED, order.getStatus());
        assertNotNull(order.getCreatedAt());
        assertEquals(car, order.getCar());
    }
}
