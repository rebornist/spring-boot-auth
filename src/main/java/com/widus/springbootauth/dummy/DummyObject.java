package com.widus.springbootauth.dummy;

import com.widus.springbootauth.user.UserDao;
import com.widus.springbootauth.user.UserEnum;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

/**
 * Created by Sshs0702 on 2021. 4. 21.
 *
 * Dummy Object
 * 테스트용 더미 객체
 */
public class DummyObject {

    protected UserDao newUser(Long id, String username) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("1234");
        return UserDao.builder()
                .id(id)
                .username(username)
                .password(encodedPassword)
                .email(username + "@gmail.com")
                .role(UserEnum.VISITOR)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected UserDao newMockUser(Long id, String username) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("1234");
        return UserDao.builder()
                .id(id)
                .username(username)
                .password(encodedPassword)
                .email(username + "@gmail.com")
                .role(UserEnum.VISITOR)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
