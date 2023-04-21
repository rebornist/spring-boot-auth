package com.widus.springbootauth.dummy;

import com.widus.springbootauth.user.UserDao;
import com.widus.springbootauth.user.UserEnum;
import com.widus.springbootauth.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by Sshs0702 on 2021. 4. 21.
 *
 * Dummy Configuration
 */
@Configuration
public class DummyDevInit {

    @Profile("dev")
    @Bean
    CommandLineRunner init(UserRepository userRepository) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");

        return args -> {
            UserDao user = userRepository.save(UserDao.builder()
                    .username("user")
                    .password(encPassword)
                    .email("user@gmail.com")
                    .role(UserEnum.VISITOR)
                    .build());
        };
    }
}
