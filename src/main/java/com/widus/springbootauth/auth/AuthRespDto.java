package com.widus.springbootauth.auth;

import com.widus.springbootauth.user.UserDao;
import com.widus.springbootauth.util.CustomDateUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Sshs0702 on 2023. 4. 21.
 *
 * 유저 응답 Dto
 *
 */
public class AuthRespDto {
    @Setter
    @Getter
    public static class SigninRespDto {
        private Long id;
        private String username;
        private String createdAt;

        public SigninRespDto(UserDao user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.createdAt = CustomDateUtil.toStringFormat(user.getCreatedAt());
        }
    }
    @ToString
    @Getter
    @Setter
    public static class SignupRespDto {
        private Long id;
        private String username;
        private String createdAt;

        public SignupRespDto(UserDao user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.createdAt = CustomDateUtil.toStringFormat(user.getCreatedAt());
        }
    }
}
