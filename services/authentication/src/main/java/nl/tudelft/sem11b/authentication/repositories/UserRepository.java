package nl.tudelft.sem11b.authentication.repositories;

import nl.tudelft.sem11b.authentication.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByNetId(String netId);
}
