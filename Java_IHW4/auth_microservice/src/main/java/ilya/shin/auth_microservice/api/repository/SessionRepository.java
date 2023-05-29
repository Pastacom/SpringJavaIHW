package ilya.shin.auth_microservice.api.repository;

import ilya.shin.auth_microservice.api.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository  extends JpaRepository<Session, Long> {
}
