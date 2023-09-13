package fiveguys.webide.repository.user;

import fiveguys.webide.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
