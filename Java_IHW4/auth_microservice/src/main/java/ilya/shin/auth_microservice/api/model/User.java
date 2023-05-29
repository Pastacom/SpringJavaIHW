package ilya.shin.auth_microservice.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(schema = "auth", name = "user",
       uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
                           })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", unique = true, length = 50, nullable = false)
    private String username;

    @Column(name = "email", unique = true, length = 100, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    @JsonIgnore
    @Column(name = "created_at")
    private Timestamp createdAt;

    @JsonIgnore
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Session> sessionList = new ArrayList<>();

    public User() {}

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }


    public String getEmail() {
        return email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public List<Session> getSessions() {
        return sessionList;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
