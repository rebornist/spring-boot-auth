package com.widus.springbootauth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.widus.springbootauth.ex.CustomApiException;
import com.widus.springbootauth.user.UserDao;
import com.widus.springbootauth.user.UserDetail;
import com.widus.springbootauth.user.UserEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
public class JwtService {

    @Autowired
    private static JwtRepository jwtRepository;

    /**
     * JWT Access 토큰 생성
     */
    public static JwtDto createAccessToken(UserDetail user, String ip) {

        // 만료일자
        Date expriesAt = new Date(System.currentTimeMillis() + JwtVo.ACCESS_EXPIRATION_TIME);

        // ACCESS 토큰 생성
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expriesAt)
                .withClaim("id", user.getUser().getId())
                .withClaim("ip", ip)
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
    public static UserDetail verifyAccessToken(String token, String currentIp) {

        // 토큰 복호화
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtVo.SECRET.getBytes()))
                .build()
                .verify(token.replace(JwtVo.TOKEN_PREFIX, ""));

        Long id = decodedJWT.getClaim("id").asLong();
        String ip = decodedJWT.getClaim("ip").asString();
        String role = decodedJWT.getClaim("role").asString();

        // IP 검증
        if (!currentIp.equals(ip)) {
            return null;
        }

        // 만료일자 검증
        Date expriesAt = decodedJWT.getExpiresAt();
        if (expriesAt == null || expriesAt.before(new Date(System.currentTimeMillis()))) {

            // 만료일자가 지났을 경우 Refresh 토큰을 검증한다.
            Optional<JwtDao> refreshToken = jwtRepository.findById(id);
            if (!refreshToken.isPresent()) {
                return null;
            }
            UserDetail user = verifyRefreshToken(refreshToken.get().getToken());

            if (user == null) {
                // 이후 Refresh 토큰이 만료되었을 경우 Null을 반환하고
                return null;
            } else {
                // 만료되지 않았을 경우 새로운 Access 토큰을 발급한다.
                JwtDto newAccessToken = createAccessToken(user, refreshToken.get().getIp());
                return new UserDetail(UserDao.builder()
                        .id(newAccessToken.getId())
                        .role(newAccessToken.getRole())
                        .build());
            }
        } else {
            return new UserDetail(UserDao.builder()
                    .id(id)
                    .token(token)
                    .role(UserEnum.valueOf(role))
                    .build());
        }
    }

    /**
     * JWT Refresh 토큰 생성
     */
    public static void createRefreshToken(UserDetail user, String ip) {

        // 만료일자
        Date expriesAt = new Date(System.currentTimeMillis() + JwtVo.REFRESH_EXPIRATION_TIME);

        // REFRESH 토큰 생성
        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expriesAt)
                .sign(Algorithm.HMAC512(JwtVo.SECRET.getBytes()));

        // TokenRespDto에 REFRESH 토큰 저장
        JwtDto jwtDto = JwtDto.builder()
                .id(user.getUser().getId())
                .expiresAt(expriesAt)
                .role(user.getUser().getRole())
                .ip(ip)
                .token(JwtVo.TOKEN_PREFIX + refreshToken)
                .build();
        saveRefreshToken(jwtDto);
    }

    /**
     * JWT Refresh 토큰 저장
     */
    public static void saveRefreshToken(JwtDto jwtDto) {
        jwtRepository.save(jwtDto.toEntity(jwtDto));
    }

    /**
     * JWT Refresh 토큰 검증
     */
    public static UserDetail verifyRefreshToken(String token) {

        // 토큰 복호화
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtVo.SECRET.getBytes()))
                .build()
                .verify(token.replace(JwtVo.TOKEN_PREFIX, ""));
        
        Long id = decodedJWT.getClaim("id").asLong();
        String role = decodedJWT.getClaim("role").asString();

        // 만료일자 검증
        Date expriesAt = decodedJWT.getExpiresAt();

        // 만료일자가 지났을 경우
        if (expriesAt == null || expriesAt.before(new Date(System.currentTimeMillis()))) {
            // Null을 반환하고
            return null;
        } else {
            // 만료되지 않았을 경우 UserDetail 객체를 반환한다.
            return new UserDetail(UserDao.builder()
                    .id(id)
                    .role(UserEnum.valueOf(role))
                    .build());
        }
    }

}
