package ilya.shin.order_microservice.api.repository;

import ilya.shin.order_microservice.api.model.OrderDish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDishRepository extends JpaRepository<OrderDish, Long> {
    List<OrderDish> findAllByOrderId(Long id);
}
