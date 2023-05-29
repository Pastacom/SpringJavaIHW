package ilya.shin.auth_microservice.api.service;

import ilya.shin.auth_microservice.api.dto.UserInfoResponse;
import ilya.shin.auth_microservice.api.model.Session;
import ilya.shin.auth_microservice.api.model.User;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserInfoService {

    public boolean CheckSessions(User user) {
        for (Session session : user.getSessions()) {
            if (session.getExpiresAt().after(new Date())) {
                return true;
            }
        }
        return false;
    }

    private Integer CountSessions(User user) {
        int num = 0;
        for (Session session : user.getSessions()) {
            if (session.getExpiresAt().after(new Date())) {
                ++num;
            }
        }
        return num;
    }
    public UserInfoResponse GetResponse(User user) {
        return new UserInfoResponse(user.getId(), user.getUsername(), user.getEmail(),
                user.getRole(), user.getCreatedAt(), CheckSessions(user), CountSessions(user));
    }
}
