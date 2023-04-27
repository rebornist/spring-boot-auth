package com.widus.springbootauth.jwt;

import com.widus.springbootauth.user.UserEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by Sshs0702 on 2021. 4. 27.
 *
 * Jwt Dto
 */
@Getter
@Setter
@Builder
public class JwtDto {
    @Id
    private Long id;
    private String ip;
    private UserEnum role;
    private Date expiresAt;
    private String token;
    public JwtDao toEntity(JwtDto jwtDto) {
        return JwtDao.builder()
                .id(jwtDto.getId())
                .ip(jwtDto.getIp())
                .role(jwtDto.getRole())
                .expiredAt(jwtDto.getExpiresAt())
                .token(jwtDto.getToken())
                .createdAt(LocalDateTime.now())
                .build();
    }

}
