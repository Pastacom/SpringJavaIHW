package ilya.shin.order_microservice.api.service;

import ilya.shin.order_microservice.api.repository.DishRepository;
import ilya.shin.order_microservice.api.model.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class DishService {

    @Autowired
    private DishRepository dishRepository;

    public void AddDish(Dish dish) {
        dishRepository.save(dish);
    }

    public Optional<Dish> GetDish(Long id) {
        return dishRepository.findById(id);
    }

    public void UpdateDishInfo(Dish dish) {
        dishRepository.save(dish);
    }

    public void DeleteDish(Long id) {
        dishRepository.deleteById(id);
    }

    public void ChangeDishQuantity(Dish dish, Integer quantity) {
        dish.setQuantity(dish.getQuantity() + quantity);
        dish.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        dish.setIsAvailable(dish.getQuantity() != 0);
        dishRepository.save(dish);
    }

    public List<Dish> GetMenu() {
        return dishRepository.GetMenu();
    }
}
