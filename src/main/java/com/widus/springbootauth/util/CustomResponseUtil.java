package com.widus.springbootauth.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.widus.springbootauth.web.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Sshs0702 on 2023. 3. 23..
 *
 * 인증 시 사용할 Response Util
 *
 */
public class CustomResponseUtil {

    public static final Logger log = LoggerFactory.getLogger(CustomResponseUtil.class);

    public static void success(HttpServletResponse response, Object dto) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            ResponseDto<?> responseDto = new ResponseDto<>(HttpStatus.OK.value(), "로그인 성공!", dto);

            String responseBody = mapper.writeValueAsString(responseDto);

            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(responseBody);
        } catch (Exception e) {
            log.error("CustomResponseUtil.success() Exception: {}", e.getMessage());
        }
    }

    public static void fail(HttpServletResponse response, String message , HttpStatus httpStatus) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            ResponseDto<?> responseDto = new ResponseDto<>(-1, message, null);

            String responseBody = mapper.writeValueAsString(responseDto);

            response.setContentType("application/json;charset=utf-8");
            response.setStatus(httpStatus.value());
            response.getWriter().println(responseBody);
        } catch (Exception e) {
            log.error("CustomResponseUtil.fail() Exception: {}", e.getMessage());
        }
    }
}
