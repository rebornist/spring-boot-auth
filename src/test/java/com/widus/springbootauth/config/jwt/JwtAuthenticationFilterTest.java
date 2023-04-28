package com.widus.springbootauth.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.widus.springbootauth.dummy.DummyObject;
import com.widus.springbootauth.jwt.JwtVo;
import com.widus.springbootauth.user.UserRepository;
import com.widus.springbootauth.auth.AuthReqDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Sshs0702 on 2023. 4. 26.
 *
 * JWT 인증 필터 테스트
 */
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class JwtAuthenticationFilterTest extends DummyObject {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() throws Exception {
        userRepository.save(newUser(1L, "user1"));
    }

    @Test
    public void successfulAuthentication_test() throws Exception {

        // given
        AuthReqDto.SigninReqDto signinReqDto = new AuthReqDto.SigninReqDto();
        signinReqDto.setUsername("user1");
        signinReqDto.setPassword("1234");
        String requestBody = objectMapper.writeValueAsString(signinReqDto);
        System.out.println("테스트 : " + requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        String jwtToken = resultActions.andReturn().getResponse().getHeader(JwtVo.HEADER);
        System.out.println("테스트 성공: " + responseBody);
        System.out.println("테스트 성공: " + jwtToken);

        // then
        resultActions.andExpect(status().isOk());
        assertNotNull(jwtToken);
        assertTrue(jwtToken.startsWith(JwtVo.TOKEN_PREFIX));

    }
    @Test
    public void failedAuthentication_test() throws Exception {

        // given
        AuthReqDto.SigninReqDto signinReqDto = new AuthReqDto.SigninReqDto();
        signinReqDto.setUsername("user1");
        signinReqDto.setPassword("12345");
        String requestBody = objectMapper.writeValueAsString(signinReqDto);
        System.out.println("테스트 : " + requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        String jwtToken = resultActions.andReturn().getResponse().getHeader(JwtVo.HEADER);
        System.out.println("테스트 실패: " + responseBody);
        System.out.println("테스트 실패: " + jwtToken);

        // then
        resultActions.andExpect(status().isUnauthorized());

    }

}
