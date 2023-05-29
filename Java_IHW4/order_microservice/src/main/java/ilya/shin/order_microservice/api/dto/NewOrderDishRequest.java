package ilya.shin.order_microservice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewOrderDishRequest {

    private Long userId;

    private Map<Long, Integer> dishes;

    private String specialRequests;

}
