package ilya.shin.auth_microservice.api.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "auth", name = "session")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "session_token", nullable = false)
    private String sessionToken;

    @Column(name = "expires_at", nullable = false)
    private Date expiresAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Session() {}

    public Session(String sessionToken, Date expiresAt, User user) {
        this.sessionToken = sessionToken;
        this.expiresAt = expiresAt;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public User getUser() {
        return user;
    }

}
