package ilya.shin.order_microservice.api.controller;

import ilya.shin.order_microservice.api.dto.NewOrderDishRequest;
import ilya.shin.order_microservice.api.model.Order;
import ilya.shin.order_microservice.api.model.Status;
import ilya.shin.order_microservice.api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Optional;

@RestController
@RequestMapping("api/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping(path="/getOrder/{id}")
    public ResponseEntity<?> GetOrder(@PathVariable Long id) {
        Optional<Order> order = orderService.GetOrder(id);
        if (order.isPresent()) {
            return new ResponseEntity<>(order.get(), HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: no order with such id.");
        }
    }

    @PostMapping(path="/makeOrder")
    public ResponseEntity<?> MakeOrder(@RequestBody NewOrderDishRequest order) {
        try {
            if (orderService.CheckOrder(order)) {
                orderService.AddOrder(order);
                return new ResponseEntity<>(order, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Error: one of the dish-order's quantity is greater than is stored.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PutMapping(path="/cancelOrder/{id}")
    public ResponseEntity<?> CancelOrder(@PathVariable Long id) {
        Optional<Order> orderWrapper = orderService.GetOrder(id);
        if (orderWrapper.isPresent()) {
            Order order = orderWrapper.get();
            order.setStatus(Status.CANCELED.getStatus());
            order.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            orderService.saveOrder(order);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: no order with such id.");
        }
    }
}
