package com.widus.springbootauth.user;

import com.widus.springbootauth.ex.CustomApiException;
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
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public UserRespDto.SignupRespDto signup(UserReqDto.SignupReqDto signupReqDto) {

        // 이미 존재하는 유저인지 확인
        Optional<UserDao> userOP = userRepository.findByUsername(signupReqDto.getUsername());

        // 이미 존재하는 유저일 경우 예외 발생
        if (userOP.isPresent()) {
            throw new CustomApiException("이미 존재하는 유저입니다.");
        }

        // 패스워드 인코딩
        UserDao userPS = userRepository.save(signupReqDto.toEntity(passwordEncoder));

        return new UserRespDto.SignupRespDto(userPS);
    }

    // 로그인
    @Transactional
    public UserRespDto.SigninRespDto signin(UserReqDto.SigninReqDto signinReqDto) {

        // 존재하는 유저인지 확인
        Optional<UserDao> userOP = userRepository.findByUsername(signinReqDto.getUsername());

        // 존재하는 유저가 아닐 경우 예외 발생
        if (userOP.isPresent()) {
            throw new CustomApiException("이미 존재하는 유저입니다.");
        }

        // 패스워드 확인
        if (!passwordEncoder.matches(signinReqDto.getPassword(), userOP.get().getPassword())) {
            throw new CustomApiException("패스워드가 일치하지 않습니다.");
        }

        return new UserRespDto.SigninRespDto(userOP.get());
    }
}
