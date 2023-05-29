package ilya.shin.order_microservice.api.repository;

import ilya.shin.order_microservice.api.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    @Query("SELECT dish FROM Dish dish WHERE dish.isAvailable=true")
    List<Dish> GetMenu();


}
