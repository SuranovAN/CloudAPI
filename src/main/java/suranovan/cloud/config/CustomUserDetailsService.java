package suranovan.cloud.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suranovan.cloud.repository.user.UserRepositoryMyImpl;

@Component
@Transactional
public class CustomUserDetailsService implements UserDetailsService{

    UserRepositoryMyImpl userRepository;

    public CustomUserDetailsService(UserRepositoryMyImpl userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        try {
            var user = userRepository.findByLoginEquals(login);

            if (user == null){
                throw new UsernameNotFoundException("No user found with username: " + login);
            }

            return new User(user.getLogin(), user.getPassword(), user.getRoles());
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
