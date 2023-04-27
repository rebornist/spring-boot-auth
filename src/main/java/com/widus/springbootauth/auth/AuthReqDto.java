package com.widus.springbootauth.auth;

import com.widus.springbootauth.user.UserDao;
import com.widus.springbootauth.user.UserEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by Sshs0702 on 2023. 3. 23.
 *
 * 유저 인증 요청 DTO
 *
 */
public class AuthReqDto {

    // Signup Request DTO
    @Getter
    @Setter
    public static class SignupReqDto {
        
        // 유저명 유효성 검사 추가
        @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "아이디는 4~20자의 영문 대소문자와 숫자로만 입력 가능합니다.")
        @NotEmpty
        private String username;

        // 비밀번호 유효성 검사 추가
        @NotEmpty
        @Size(min = 8, max = 20, message = "비밀번호는 8~20자 이내로 입력해주세요.")
        private String password;
        
        // 이메일 유효성 검사 추가
        @Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$", message = "이메일 형식이 올바르지 않습니다.")
        @NotEmpty
        private String email;

        // 성명 유효성 검사 추가
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,20}$", message = "성명은 2~20자의 한글, 영문, 숫자로만 입력 가능합니다.")
        @NotEmpty
        private String fullname;

        // 기본 권한 부여 및 패스워드 암호화
        public UserDao toEntity(BCryptPasswordEncoder passwordEncoder) {
            return UserDao.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .role(UserEnum.VISITOR)
                    .build();
        }
    }

    // 로그인 요청 DTO
    @Getter
    @Setter
    public static class SigninReqDto {

        // 유저명 유효성 검사 추가
        // @Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$", message = "이메일 형식이 올바르지 않습니다.")
        @NotEmpty
        private String username;

        // 비밀번호 유효성 검사 추가
        @NotEmpty
        // @Size(min = 8, max = 20, message = "비밀번호는 8~20자 이내로 입력해주세요.")
        private String password;

    }

}
