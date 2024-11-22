package college.devops.Devops.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import college.devops.Devops.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
