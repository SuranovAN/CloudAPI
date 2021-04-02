package suranovan.cloud.repository.role;

import org.springframework.data.jpa.repository.JpaRepository;
import suranovan.cloud.model.Roles;

public interface IRoleRepository extends JpaRepository<Roles, Integer> {
    Roles findByIdEquals(int id);
}
