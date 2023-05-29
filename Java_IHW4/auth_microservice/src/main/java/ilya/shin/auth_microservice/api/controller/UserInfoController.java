package ilya.shin.auth_microservice.api.controller;

import ilya.shin.auth_microservice.api.model.User;
import ilya.shin.auth_microservice.api.repository.UserRepository;
import ilya.shin.auth_microservice.api.security.jwt.JwtUtils;
import ilya.shin.auth_microservice.api.service.UserInfoService;
import ilya.shin.auth_microservice.api.utility.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/info")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserInfoController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    UserValidator userValidator;
    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/getInfo")
    public ResponseEntity<?> GetInfo(@RequestHeader("Authorization") String jwt) {
        String email = jwtUtils.getEmailFromJwtToken(jwt.substring(7));
        Optional<User> userWrapper = userRepository.findByEmail(email);
        if (userWrapper.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(userInfoService.GetResponse(userWrapper.get()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: user with such email doesn't exist");
        }
    }
    @GetMapping("/is-valid-customer")
    public Pair<Boolean, String> isUserValidCustomer(@RequestParam("id") Long userId) {
        try {
            userValidator.checkIfUserValidCustomer(userId);
            return Pair.of(true, "");
        } catch (Exception exception) {
            return Pair.of(false, exception.getMessage());
        }
    }

    @GetMapping("/is-valid-manager")
    public Pair<Boolean, String> isUserValidManager(@RequestParam("token") String token) {
        try {
            if (!jwtUtils.validateToken(token.replace("Bearer ", ""))) {
                throw new IllegalArgumentException("Incorrect bearer token provided!");
            }
            userValidator.checkIfUserValidManager(token.substring(7));
            return Pair.of(true, "");
        } catch (Exception exception) {
            return Pair.of(false, exception.getMessage());
        }
    }

    @GetMapping("/is-valid")
    public Pair<Boolean, String> isUserValid(@RequestParam("token") String token) {
        try {
            if (!jwtUtils.validateToken(token.replace("Bearer ", ""))) {
                throw new IllegalArgumentException("Incorrect bearer token provided!");
            }
            userValidator.checkIfUserValid(token.substring(7));
            return Pair.of(true, "");
        } catch (Exception exception) {
            return Pair.of(false, exception.getMessage());
        }
    }
}
