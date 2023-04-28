package com.widus.springbootauth.config.jwt;

import com.widus.springbootauth.jwt.*;
import com.widus.springbootauth.user.UserDao;
import com.widus.springbootauth.user.UserDetail;
import com.widus.springbootauth.user.UserDto;
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

    @Autowired
    private JwtService jwtService;

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
        JwtDto jwtDto = jwtService.CreateAccessToken(loginUser, ip);

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
        JwtDto jwtDto = jwtService.CreateAccessToken(loginUser, ip);

        String reqIp = "192.168.1.1";

        // when
        UserDto verifyUser = jwtService.VerifyAccessToken(jwtDto.getToken(), reqIp);
        UserDetail newUser = new UserDetail(verifyUser.toEntity(verifyUser));

        // then
        assertTrue(newUser.getUser().getId().equals(loginUser.getUser().getId()));
        assertTrue(newUser.getUser().getRole().equals(loginUser.getUser().getRole()));
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
        JwtDto jwtDto = jwtService.CreateRefreshToken(loginUser, ip);
        jwtRepository.save(jwtDto.toEntity(jwtDto));

        JwtDao newDao = jwtRepository.findById(loginUser.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. id=" + loginUser.getUser().getId()));

        System.out.println("newDao.getRefreshToken() = " + newDao.getIp());

        // then
//        assertTrue(newDao != null);

    }
}

