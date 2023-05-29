package ilya.shin.order_microservice.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(schema = "dishes", name = "order_dish")
public class OrderDish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @JsonIgnore
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "dish_id", nullable = false)
    private Long dishId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getOrder() {
        return orderId;
    }

    public void setOrder(Long order) {
        this.orderId = order;
    }

    public Long getDish() {
        return dishId;
    }

    public void setDish(Long dish) {
        this.dishId = dish;
    }
}
