package ilya.shin.auth_microservice.api.service;

import ilya.shin.auth_microservice.api.model.Role;
import ilya.shin.auth_microservice.api.model.Session;
import ilya.shin.auth_microservice.api.model.User;
import ilya.shin.auth_microservice.api.repository.SessionRepository;
import ilya.shin.auth_microservice.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static Boolean ValidateEmail(String email) {
        String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }

    public void RegisterUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void LoginUser(User user, String jwt, Date expiresAt) {
        userRepository.save(user);
        sessionRepository.save(new Session(jwt, expiresAt, user));
    }

    public Boolean CheckUsername(String username) {
        return !userRepository.existsByUsername(username);
    }

    public Boolean CheckEmail(String email) {
        return !userRepository.existsByEmail(email);
    }

    public Boolean CheckRole(String role) {
        return Objects.equals(Role.CHEF.getRole(), role) ||
                Objects.equals(Role.MANAGER.getRole(), role) ||
                Objects.equals(Role.CUSTOMER.getRole(), role);
    }
}
