package ilya.shin.auth_microservice.api.utility;

import ilya.shin.auth_microservice.api.model.Role;
import ilya.shin.auth_microservice.api.model.User;
import ilya.shin.auth_microservice.api.repository.UserRepository;
import ilya.shin.auth_microservice.api.security.jwt.JwtUtils;
import ilya.shin.auth_microservice.api.service.UserInfoService;
import org.hibernate.SessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class UserValidator {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private JwtUtils jwtUtils;

    public void checkIfUserValid(String token) throws Exception {
        User user = userRepository
                .findByEmail(jwtUtils.getEmailFromJwtToken(token))
                .orElseThrow(() -> new NoSuchElementException("User with token provided does not exist!"));

        if (!userInfoService.CheckSessions(user)) {
            throw new SessionException("Session of user has expired!");
        }
    }

    public void checkIfUserValidManager(String token) throws Exception {
        User user = userRepository
                .findByEmail(jwtUtils.getEmailFromJwtToken(token))
                .orElseThrow(() -> new NoSuchElementException("User with token provided does not exist!"));

        if (!userInfoService.CheckSessions(user)) {
            throw new SessionException("Session of user has expired!");
        }

        if (!Objects.equals(user.getRole(), Role.MANAGER.getRole())) {
            throw new IllegalAccessException("User has role: <" + user.getRole() + ">, but required 'manager'");
        }
    }

    public void checkIfUserValidCustomer(Long userId) throws Exception {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id: <" + userId + "> does not exist"));

        if (!userInfoService.CheckSessions(user)) {
            throw new SessionException("Session of user has expired!");
        }

        if (!Objects.equals(user.getRole(), Role.CUSTOMER.getRole())) {
            throw new IllegalAccessException("User has role: <" + user.getRole() + ">, but required 'customer'");
        }
    }
}
