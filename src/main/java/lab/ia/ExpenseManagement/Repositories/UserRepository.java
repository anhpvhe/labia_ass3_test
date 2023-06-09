package lab.ia.ExpenseManagement.Repositories;

import lab.ia.ExpenseManagement.Exceptions.ResourceNotFoundException;
import lab.ia.ExpenseManagement.Models.User;
import lab.ia.ExpenseManagement.Security.UserPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    void deleteByUsername(String username);

    default User getUserByUserPrincipal(UserPrincipal currenUser) {
        return getUserByUsername(currenUser.getUsername());
    }

    default User getUserByUsername(String username) {
        return this.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }
}
