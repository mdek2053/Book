package nl.tudelft.sem11b.authentication.repositories;

import java.util.Optional;

import nl.tudelft.sem11b.authentication.entities.User;
import nl.tudelft.sem11b.data.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Provides a userRepository which stores the users of the system.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByNetId(String netId);

    Optional<User> findUserById(Long id);
}
