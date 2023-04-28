package com.widus.springbootauth.config.jwt;

import com.widus.springbootauth.jwt.*;
import com.widus.springbootauth.user.UserDao;
import com.widus.springbootauth.user.UserDetail;
import com.widus.springbootauth.user.UserEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by Sshs0702 on 2023. 4. 26.
 *
 * JWT 토큰 생성 및 검증 테스트
 */
public class JwtServiceTest {

    @Autowired
    private JwtRepository jwtRepository;

    @Test
    public void createToken_test() throws Exception {
        // given
        UserDao user = UserDao.builder()
                .id(1L)
                .role(UserEnum.VISITOR)
                .build();
        UserDetail loginUser = new UserDetail(user);
        String ip = "192.168.1.1";

        // when
        JwtDto jwtDto = JwtService.createAccessToken(loginUser, ip);

        // then
        assertTrue(jwtDto.getToken().startsWith(JwtVo.TOKEN_PREFIX));
    }

    @Test
    public void verifyToken_test() throws Exception {
        // given
        UserDao user = UserDao.builder()
                .id(1L)
                .role(UserEnum.VISITOR)
                .build();
        UserDetail loginUser = new UserDetail(user);
        String ip = "192.168.1.1";
        JwtDto jwtDto = JwtService.createAccessToken(loginUser, ip);

        String reqIp = "192.168.1.1";

        // when
        UserDetail verifyUser = JwtService.verifyAccessToken(jwtDto.getToken(), reqIp);

        // then
        assertTrue(verifyUser.getUser().getId().equals(loginUser.getUser().getId()));
        assertTrue(verifyUser.getUser().getRole().equals(loginUser.getUser().getRole()));
    }

    @Test
    public void createRefreshToken_test() throws Exception {

        // given
        UserDao user = UserDao.builder()
                .id(1L)
                .role(UserEnum.VISITOR)
                .build();
        UserDetail loginUser = new UserDetail(user);

        String ip = "192.168.1.1";

        // when
        JwtDto jwtDto = JwtService.createRefreshToken(loginUser, ip);
        jwtRepository.save(jwtDto.toEntity(jwtDto));

        JwtDao newDao = jwtRepository.findById(loginUser.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + loginUser.getUser().getId()));

        System.out.println("newDao.getRefreshToken() = " + newDao.getIp());

        // then
//        assertTrue(newDao != null);

    }
}

