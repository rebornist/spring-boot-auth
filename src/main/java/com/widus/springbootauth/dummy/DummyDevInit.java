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
 * dev 환경에서만 실행되는 더미 데이터 생성
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
                    .id(1L)
                    .username("sshs0702")
                    .password(encPassword)
                    .email("sshs0702@gmail.com")
                    .role(UserEnum.VISITOR)
                    .build());
        };
    }
}
