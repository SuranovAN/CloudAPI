package suranovan.cloud.db;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import suranovan.cloud.repository.user.UserRepositoryMyImpl;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DBTest {

    @Autowired
    UserRepositoryMyImpl userRepository;

    @Test
    void testConnect() {
        String userName = "user1";
        Assertions.assertEquals(1, userRepository.findByLoginEquals(userName).getId());
        Assertions.assertEquals("user1", userRepository.findByLoginEquals(userName).getLogin());
        System.out.println(userRepository.findAllBy().toString());
    }
}
