package suranovan.cloud.repository.role;

import org.springframework.stereotype.Repository;
import suranovan.cloud.model.Roles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class RoleRepositoryImpl {
    @PersistenceContext
    EntityManager entityManager;

    public void initialize(){
        List<String> roles = List.of("USER", "ADMIN", "OWNER", "CO-OWNER");
        AtomicInteger y = new AtomicInteger(0);
        roles.forEach(e -> {
            var role = Roles.builder().id(y.incrementAndGet()).role("ROLE_" + e).build();
            entityManager.persist(role);
        });
    }
}
