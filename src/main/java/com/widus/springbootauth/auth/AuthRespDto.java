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
@Getter
@Setter
@ToString
public class AuthRespDto {
    private String token;

    public AuthRespDto(String token) {
        this.token = token;
    }
}
