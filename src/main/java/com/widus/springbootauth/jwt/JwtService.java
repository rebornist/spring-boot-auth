package com.widus.springbootauth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.widus.springbootauth.user.UserDetail;
import com.widus.springbootauth.user.UserDto;
import com.widus.springbootauth.user.UserEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * Created by Widus on 2023. 3. 23.
 *
 * JWT 인증 서비스
 * JWT Access 토큰 생성, 검증, 갱신
 * JWT Refresh 토큰 생성, 검증
 * 추후 Redis를 사용하여 토큰을 관리할 수 있도록 개선할 예정
 */
@Service
public class JwtService {

    @Autowired
    private JwtRepository jwtRepository;

    /**
     * JWT Access 토큰 생성
     */
    public JwtDto CreateAccessToken(UserDetail user, String ip) {

        // 만료일자
        Date expriesAt = new Date(System.currentTimeMillis() + JwtVo.ACCESS_EXPIRATION_TIME);

        // ACCESS 토큰 생성
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expriesAt)
                .withClaim("id", user.getUser().getId())
                .withClaim("role", user.getUser().getRole().name())
                .sign(Algorithm.HMAC512(JwtVo.SECRET.getBytes()));

        // TokenRespDto에 ACCESS 토큰 저장
        return JwtDto.builder()
                .id(user.getUser().getId())
                .expiresAt(expriesAt)
                .role(user.getUser().getRole())
                .ip(ip)
                .token(JwtVo.TOKEN_PREFIX + accessToken)
                .build();
    }

    /**
     * JWT Access 토큰 검증
     */
    public UserDto VerifyAccessToken(String token, String currentIp) {

        // 토큰 복호화
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtVo.SECRET.getBytes()))
                .build()
                .verify(token.replace(JwtVo.TOKEN_PREFIX, ""));

        Long id = decodedJWT.getClaim("id").asLong();
        String role = decodedJWT.getClaim("role").asString();

        // 만료일자 검증
        Date expriesAt = decodedJWT.getExpiresAt();
        if (expriesAt == null || expriesAt.before(new Date(System.currentTimeMillis()))) {

            // 만료일자가 지났을 경우 Refresh 토큰을 검증한다.
            Optional<JwtDao> refreshToken = jwtRepository.findById(id);
            if (!refreshToken.isPresent()) {
                return null;
            }

            UserDto userDto = VerifyRefreshToken(refreshToken.get().getToken());
            UserDetail user = new UserDetail(userDto.toEntity(userDto));

            // 현재 IP와 토큰에 저장된 IP가 일치하는지 검증 후 일치하지 않을 경우 Null을 반환한다.
            if (!currentIp.equals(refreshToken.get().getIp())) {
                return null;
            }

            // 이후 Refresh 토큰이 만료되었을 경우 Null을 반환하고
            // 만료되지 않았을 경우 새로운 Access 토큰을 발급한다.
            if (user == null) {
                return null;
            } else {
                JwtDto newAccessToken = CreateAccessToken(user, currentIp);
                return UserDto.builder()
                        .id(newAccessToken.getId())
                        .token(newAccessToken.getToken())
                        .role(newAccessToken.getRole())
                        .build();
            }
        } else {
            return UserDto.builder()
                    .id(id)
                    .token(token)
                    .role(UserEnum.valueOf(role))
                    .build();
        }
    }

    /**
     * JWT Refresh 토큰 생성
     */
    public JwtDto CreateRefreshToken(UserDetail user, String ip) {

        // 만료일자
        Date expriesAt = new Date(System.currentTimeMillis() + JwtVo.REFRESH_EXPIRATION_TIME);

        // REFRESH 토큰 생성
        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expriesAt)
                .sign(Algorithm.HMAC512(JwtVo.SECRET.getBytes()));

        // TokenRespDto에 REFRESH 토큰 저장
        return JwtDto.builder()
                .id(user.getUser().getId())
                .expiresAt(expriesAt)
                .role(user.getUser().getRole())
                .ip(ip)
                .token(JwtVo.TOKEN_PREFIX + refreshToken)
                .build();
    }

    /**
     * JWT Refresh 토큰 검증
     */
    public UserDto VerifyRefreshToken(String token) {

        // 토큰 복호화
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtVo.SECRET.getBytes()))
                .build()
                .verify(token.replace(JwtVo.TOKEN_PREFIX, ""));
        
        Long id = decodedJWT.getClaim("id").asLong();
        String role = decodedJWT.getClaim("role").asString();

        // 만료일자 검증
        Date expriesAt = decodedJWT.getExpiresAt();

        // 만료일자가 지났거나 존재하지 않을 경우 Null을 반환한다.
        if (expriesAt == null || expriesAt.before(new Date(System.currentTimeMillis()))) {
            jwtRepository.deleteById(id);
            return null;
        } else {
            return UserDto.builder() // 만료되지 않았을 경우 UserDetail 객체를 반환한다.
                    .id(id)
                    .token(token)
                    .role(UserEnum.valueOf(role))
                    .build();
        }
    }



}
