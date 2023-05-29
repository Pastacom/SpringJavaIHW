package ilya.shin.order_microservice.api.controller;

import ilya.shin.order_microservice.api.model.Dish;
import ilya.shin.order_microservice.api.service.DishService;
import ilya.shin.order_microservice.api.utility.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/dishes")
public class DishController {

    @Autowired
    DishService dishService;

    @Autowired
    UserValidator userValidator;

    @GetMapping(path="/menu")
    public ResponseEntity<List<Dish>> GetMenu() {
        return new ResponseEntity<>(dishService.GetMenu(), HttpStatus.OK);
    }

    @GetMapping(path="/getDish/{id}")
    public ResponseEntity<?> GetDish(@PathVariable Long id) {
        Optional<Dish> dish = dishService.GetDish(id);
        if (dish.isPresent()) {
            return new ResponseEntity<>(dish.get(), HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: no dish with such id.");
        }
    }

    @PostMapping(path="/addDish")
    public ResponseEntity<?> AddDish(@RequestHeader("Authorization") String token,
                                     @RequestBody Dish dish) {
        try {
            userValidator.validateUserManager(token);
            if (dish.getQuantity() >= 0) {
                dish.setIsAvailable(true);
                dish.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                dish.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                dishService.AddDish(dish);
                return new ResponseEntity<>(dish, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Error: provided quantity makes dish's quantity below zero.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }

    }

    @PutMapping(path="/updateInfo/{id}")
    public ResponseEntity<?> UpdateDishInfo(@RequestHeader("Authorization") String token,
                                            @PathVariable Long id, @RequestBody Dish newDish) {
        try {
            userValidator.validateUserManager(token);
            Optional<Dish> wrappedDish = dishService.GetDish(id);
            if (wrappedDish.isPresent()) {
                if (newDish.getQuantity() >= 0) {
                    Dish dish = wrappedDish.get();
                    dish.setName(newDish.getName());
                    dish.setDescription(newDish.getDescription());
                    dish.setPrice(newDish.getPrice());
                    dish.setQuantity(newDish.getQuantity());
                    dish.setIsAvailable(newDish.getQuantity() > 0);
                    dish.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                    dishService.UpdateDishInfo(dish);
                    return new ResponseEntity<>(dish, HttpStatus.OK);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Error: provided quantity makes dish's quantity below zero.");
                }

            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: no dish with such id.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @PutMapping(path="/updateQuantity/{id}")
    public ResponseEntity<?> UpdateDishQuantity(@PathVariable Long id, @RequestParam Integer quantity) {
        try {
            Optional<Dish> wrappedDish = dishService.GetDish(id);
            if (wrappedDish.isPresent()) {
                Dish dish = wrappedDish.get();
                if (dish.getQuantity() + quantity >= 0) {
                    dishService.ChangeDishQuantity(dish, quantity);
                    return ResponseEntity.status(HttpStatus.OK).body(dish);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Error: provided quantity makes dish's quantity below zero.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: no dish with such id.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping(path="/deleteDish/{id}")
    public ResponseEntity<?> deleteDish(@PathVariable Long id) {
        try {
            if (dishService.GetDish(id).isPresent()) {
                dishService.DeleteDish(id);
                return ResponseEntity.status(HttpStatus.OK).body("Dish is deleted.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: no dish with such id.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

}