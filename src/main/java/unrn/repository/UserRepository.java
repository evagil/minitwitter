package unrn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import unrn.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUserName(String userName);

    Optional<User> findByUserNameIgnoreCase(String userName);

    boolean existsByUserNameIgnoreCase(String userName);
}

