package suranovan.cloud.repository.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import suranovan.cloud.model.UserEntity;
import suranovan.cloud.repository.role.IRoleRepository;
import suranovan.cloud.repository.role.RoleRepositoryImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class UserRepositoryMyImpl implements CommandLineRunner {

    @PersistenceContext
    EntityManager entityManager;

    PasswordEncoder encoder;
    IUserRepository IUserRepository;
    RoleRepositoryImpl roleRepository;
    IRoleRepository IRoleRepository;


    public UserRepositoryMyImpl(PasswordEncoder encoder, IUserRepository IUserRepository, RoleRepositoryImpl roleRepository, IRoleRepository IRoleRepository) {
        this.encoder = encoder;
        this.IUserRepository = IUserRepository;
        this.roleRepository = roleRepository;
        this.IRoleRepository = IRoleRepository;
    }

    private void initialize() {
        var user = UserEntity.builder()
                .id(1L)
                .login("user1")
                .email("some@mail")
                .password(encoder.encode("123"))
                .role("ROLE_USER")
                .build();
        entityManager.persist(user);

        var admin = UserEntity.builder()
                .id(2L)
                .login("admin")
                .email("some@mail2")
                .password(encoder.encode("qwe"))
                .role("ROLE_ADMIN")
                .build();
        admin.addRole(IRoleRepository.findByIdEquals(2));
        entityManager.persist(admin);
    }

    public UserEntity findDistinctByIdEquals(Long id) {
        return IUserRepository.findDistinctByIdEquals(id);
    }

    public List<UserEntity> findAllBy() {
        return IUserRepository.findAllBy();
    }

    public UserEntity findByLoginEquals(String login) {
        return IUserRepository.findByLoginEquals(login);
    }

    @Override
    public void run(String... args) throws Exception {
        roleRepository.initialize();
        initialize();
    }
}
