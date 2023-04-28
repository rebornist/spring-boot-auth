package com.widus.springbootauth.config.jwt;

import com.widus.springbootauth.jwt.JwtDto;
import com.widus.springbootauth.jwt.JwtService;
import com.widus.springbootauth.jwt.JwtVo;
import com.widus.springbootauth.user.UserDao;
import com.widus.springbootauth.user.UserDetail;
import com.widus.springbootauth.user.UserEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by Sshs0702 on 2023. 4. 26.
 *
 * JWT 인증 필터 테스트
 */
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class JwtAuthorizationFilterTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JwtService jwtService;

    @Test
    public void successfulAuthorization_test() throws Exception {

        // given
        UserDao user = UserDao.builder()
                .id(1L)
                .role(UserEnum.USER)
                .build();
        UserDetail loginUser = new UserDetail(user);

        String ip = "192.168.1.1";

        JwtDto jwtToken = jwtService.CreateAccessToken(loginUser, ip);
        System.out.println("테스트 : " + jwtToken);

        // when
        ResultActions result = mvc.perform(get("/api/auth")
                .header("Authorization", "Bearer " + jwtToken));

        // then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void failedAuthorization_test() throws Exception {

        // given

        // when
        ResultActions result = mvc.perform(get("/api/s/hello/test"));

        // then
        result.andExpect(status().isUnauthorized());

    }

    @Test
    public void managerAuthorization_test() throws Exception {

        // given
        UserDao user = UserDao.builder().id(1L).role(UserEnum.MANAGER).build();
        UserDetail loginUser = new UserDetail(user);
        String ip = "192.168.1.1";
        JwtDto jwtDto = jwtService.CreateAccessToken(loginUser, ip);
        System.out.println("테스트 : " + jwtDto.getToken());

        // when
        ResultActions result = mvc.perform(get("/api/manager/hello/test")
                .header(JwtVo.HEADER, jwtDto.getToken()));

        // then
        result.andExpect(status().isForbidden());

    }


}

