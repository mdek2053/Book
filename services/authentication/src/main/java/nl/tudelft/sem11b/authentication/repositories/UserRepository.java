package nl.tudelft.sem11b.authentication.repositories;

import nl.tudelft.sem11b.authentication.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByNetId(String netId);
}
