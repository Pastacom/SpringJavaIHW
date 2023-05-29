package ilya.shin.auth_microservice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {

    private Long id;

    private String username;

    private String email;

    private String role;

    private Timestamp accountCreatedAt;

    private Boolean isLoggedIn;

    private Integer activeSessions;
}
