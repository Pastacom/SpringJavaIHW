package ilya.shin.order_microservice.api.worker;

import ilya.shin.order_microservice.api.model.Dish;
import ilya.shin.order_microservice.api.model.Order;
import ilya.shin.order_microservice.api.model.OrderDish;
import ilya.shin.order_microservice.api.model.Status;
import ilya.shin.order_microservice.api.repository.DishRepository;
import ilya.shin.order_microservice.api.repository.OrderDishRepository;
import ilya.shin.order_microservice.api.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class OrderHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderHandler.class);

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private OrderDishRepository orderDishRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Async
    @EventListener
    public void handleOrder(Order order) {
        if (!Objects.equals(order.getStatus(), Status.WAITING.getStatus())) {
            return;
        }
        LOGGER.info("Order: №{} status: [Waiting]", order.getId());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        LOGGER.info("Order №{} status: [Cooking]", order.getId());
        order.setStatus(Status.COOKING.getStatus());
        order.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        orderRepository.save(order);

        List<OrderDish> orderDishes = orderDishRepository.findAllByOrderId(order.getId());
        for (var orderDish : orderDishes) {
            Optional<Dish> dish = dishRepository.findById(orderDish.getId());
            if (dish.isEmpty()) {
                System.out.println("Dish with order id: <" + orderDish.getId() + "> was not found");
            }

            for (int i = 1; i <= orderDish.getQuantity(); ++i) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                LOGGER.info("#{} dish №{} \"{}\" status: [Cooking]", i, dish.get().getId(), dish.get().getName());

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                LOGGER.info("#{} dish №{} \"{}\" status: [Ready]", i, dish.get().getId(), dish.get().getName());
            }
        }

        LOGGER.info("Order №{} status: [Ready]", order.getId());
        order.setStatus(Status.READY.getStatus());
        order.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        orderRepository.save(order);
    }
}
