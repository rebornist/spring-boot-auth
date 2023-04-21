package com.widus.springbootauth.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Created by Sshs0702 on 2021. 4. 21.
 *
 * Spring Config Test
 */
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SpringConfigTest {

    // 가짜 환경에 등록된 MockMvc를 사용하여 테스트를 진행한다.
    @Autowired
    private MockMvc mockMvc;

    // 서버는 일관성 있게 에러가 리턴되어야 한다.
    // 알지 못하는 에러가 노출되어서는 안된다.
    @Test
    public void authentication_test() throws Exception {

        // given

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/s/hello"));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        int httpStatus = resultActions.andReturn().getResponse().getStatus();
        System.out.println("테스트: " + responseBody);

        // then
        assertThat(httpStatus).isEqualTo(HttpStatus.OK.value());

    }
}
