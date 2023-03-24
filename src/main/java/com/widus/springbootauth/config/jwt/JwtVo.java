package com.widus.springbootauth.config.jwt;

/**
 * Created by Widus on 2023. 3. 23..
 *
 * JWT VO
 *
 */
public interface JwtVo {
    public static final String SECRET = "widus";
    public static final int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;  // 일주일
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";
}