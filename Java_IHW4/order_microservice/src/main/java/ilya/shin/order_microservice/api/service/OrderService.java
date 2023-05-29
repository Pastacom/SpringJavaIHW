package ilya.shin.order_microservice.api.service;

import ilya.shin.order_microservice.api.dto.NewOrderDishRequest;
import ilya.shin.order_microservice.api.model.Order;
import ilya.shin.order_microservice.api.model.OrderDish;
import ilya.shin.order_microservice.api.model.Status;
import ilya.shin.order_microservice.api.repository.OrderDishRepository;
import ilya.shin.order_microservice.api.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private OrderDishRepository orderDishRepository;
    @Autowired
    private DishService dishService;

    public Optional<Order> GetOrder(Long id) {
        return orderRepository.findById(id);
    }

    public Boolean CheckOrder(NewOrderDishRequest order) {
        Map<Long, Integer> orderDishSet = order.getDishes();
        for (var pair : orderDishSet.entrySet()) {
            var wrapper = dishService.GetDish(pair.getKey());
            if (wrapper.isPresent()) {
                if (wrapper.get().getQuantity() < pair.getValue()) {
                    return false;
                }
            } else {
                throw new IllegalArgumentException("Error: dish with such id doesn't exist.");
            }

        }
        return true;
    }

    public void AddOrder(NewOrderDishRequest order) {
        Order newOrder = new Order();
        newOrder.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        newOrder.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        newOrder.setStatus(Status.WAITING.getStatus());
        newOrder.setUser(order.getUserId());
        newOrder.setSpecialRequests(order.getSpecialRequests());
        orderRepository.save(newOrder);
        for (var pair : order.getDishes().entrySet()) {
            dishService.ChangeDishQuantity(dishService.GetDish(pair.getKey()).get(), -pair.getValue());
            OrderDish orderDish = new OrderDish();
            orderDish.setDish(pair.getKey());
            orderDish.setQuantity(pair.getValue());
            orderDish.setOrder(newOrder.getId());
            orderDish.setPrice(dishService.GetDish(orderDish.getDish()).get().getPrice()
                    .multiply(BigDecimal.valueOf(orderDish.getQuantity())));
            orderDishRepository.save(orderDish);
        }
        eventPublisher.publishEvent(newOrder);
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }
}
