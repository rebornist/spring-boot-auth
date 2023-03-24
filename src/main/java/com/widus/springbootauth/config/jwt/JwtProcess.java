package com.widus.springbootauth.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.widus.springbootauth.user.UserDao;
import com.widus.springbootauth.user.UserDetail;
import com.widus.springbootauth.user.UserEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by Widus on 2023. 3. 23..
 *
 * JWT 인증 프로세스
 *
 */
public class JwtProcess {

    // 로그
    public final Logger log = LoggerFactory.getLogger(this.getClass());

    // JWT 토큰 생성
    public static String createToken(UserDetail user) {
        String jwtToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVo.EXPIRATION_TIME))
                .withClaim("id", user.getUser().getId())
                .withClaim("role", user.getUser().getRole().name())
                .sign(Algorithm.HMAC512(JwtVo.SECRET.getBytes()));
        return JwtVo.TOKEN_PREFIX + jwtToken;
    }

    // JWT 토큰 검증
    public static UserDetail verifyToken(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtVo.SECRET.getBytes()))
                .build()
                .verify(token.replace(JwtVo.TOKEN_PREFIX, ""));

        Long id = decodedJWT.getClaim("id").asLong();
        String role = decodedJWT.getClaim("role").asString();
        UserDao user = UserDao.builder()
                .id(id)
                .role(UserEnum.valueOf(role))
                .build();
        return new UserDetail(user);
    }

}
