package com.widus.springbootauth.auth;

import com.widus.springbootauth.ex.CustomApiException;
import com.widus.springbootauth.user.UserDao;
import com.widus.springbootauth.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Created by Sshs0702 on 2021. 4. 21.
 * 
 * 유저 서비스
 */

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    @Transactional
    public AuthRespDto.SignupRespDto signup(AuthReqDto.SignupReqDto signupReqDto) {

        // 이미 존재하는 유저인지 확인
        Optional<UserDao> userOP = userRepository.findByUsername(signupReqDto.getUsername());

        // 이미 존재하는 유저일 경우 예외 발생
        if (userOP.isPresent()) throw new CustomApiException("이미 존재하는 유저입니다.");

        // 패스워드 인코딩
        UserDao userPS = userRepository.save(signupReqDto.toEntity(passwordEncoder));

        return new AuthRespDto.SignupRespDto(userPS);
    }

}
