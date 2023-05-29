package ilya.shin.auth_microservice.api.controller;

import ilya.shin.auth_microservice.api.model.User;
import ilya.shin.auth_microservice.api.repository.UserRepository;
import ilya.shin.auth_microservice.api.service.AuthService;
import ilya.shin.auth_microservice.api.security.jwt.JwtUtils;
import ilya.shin.auth_microservice.api.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import java.sql.Timestamp;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> RegisterNewUser(@RequestBody User user) {
        if (!authService.CheckUsername(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: username is already in use.");
        }

        if (!AuthService.ValidateEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: invalid email format.");
        }

        if (!authService.CheckEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: email is already in use.");
        }

        if (!authService.CheckRole(user.getRole())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: provided role doesn't exist.");
        }

        user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        authService.RegisterUser(user);

        return ResponseEntity.status(HttpStatus.OK)
                .body("User was successfully registered.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> LoginUser(@RequestParam String email,
                                       @RequestParam String password) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(email.substring(1, email.length()-1),
                        password.substring(1, password.length()-1)));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        Date expirationTime = jwtUtils.getExpirationTimeFromJwtToken(jwt);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Optional<User> userWrapper = userRepository.findByEmail(userDetails.getEmail());

        if (userWrapper.isPresent()) {
            User user = userWrapper.get();
            user.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            authService.LoginUser(user, jwt, expirationTime);
            return ResponseEntity.status(HttpStatus.OK)
                   .body(jwt);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: user with such email is not found.");
        }
    }

}
