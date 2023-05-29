package ilya.shin.order_microservice.api.utility;

import ilya.shin.order_microservice.api.dto.UserValidResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UserValidator {

    private static final String IS_VALID_USER_ENDPOINT = "http://localhost:8080/api/info/is-valid";
    private static final String IS_VALID_MANAGER_ENDPOINT = "http://localhost:8080/api/info/is-valid-manager/";
    private static final String IS_VALID_CUSTOMER_ENDPOINT = "http://localhost:8080/api/info/is-valid-customer/";

    @Autowired
    private WebClient webClient;

    public void validateUser(String token) throws Exception {
        UserValidResponse userValidResponseResult = isUserValid(token);
        if (!userValidResponseResult.first()) {
            throw new Exception(userValidResponseResult.second());
        }
    }

    public void validateUserManager(String token) throws Exception {
        UserValidResponse userValidResponseResult = isUserValidManager(token);
        if (!userValidResponseResult.first()) {
            throw new Exception(userValidResponseResult.second());
        }
    }

    public void validateUserCustomer(Integer id) throws Exception {
        UserValidResponse userValidResponseResult = isUserValidCustomer(id);
        if (!userValidResponseResult.first()) {
            throw new Exception(userValidResponseResult.second());
        }
    }

    private UserValidResponse isUserValid(String token) {
        return webClient.get()
                .uri(IS_VALID_USER_ENDPOINT,
                        uriBuilder -> uriBuilder.queryParam("token", token).build())
                .retrieve()
                .bodyToMono(UserValidResponse.class)
                .block();
    }

    private UserValidResponse isUserValidManager(String token) {
        return webClient.get()
                .uri(IS_VALID_MANAGER_ENDPOINT,
                        uriBuilder -> uriBuilder.queryParam("token", token).build())
                .retrieve()
                .bodyToMono(UserValidResponse.class)
                .block();
    }

    private UserValidResponse isUserValidCustomer(Integer userId) {
        return webClient.get()
                .uri(IS_VALID_CUSTOMER_ENDPOINT,
                        uriBuilder -> uriBuilder.queryParam("id", userId).build())
                .retrieve()
                .bodyToMono(UserValidResponse.class)
                .block();
    }

}
