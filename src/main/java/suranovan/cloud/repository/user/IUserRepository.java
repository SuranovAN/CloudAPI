package suranovan.cloud.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import suranovan.cloud.model.UserEntity;

import java.util.List;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findDistinctByIdEquals(Long id);

    List<UserEntity> findAllBy();

    UserEntity findByLoginEquals(String login);
}
