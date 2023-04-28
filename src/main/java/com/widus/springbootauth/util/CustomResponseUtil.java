package com.widus.springbootauth.util;

import javax.servlet.http.HttpServletResponse;

import com.widus.springbootauth.ex.CustomApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.widus.springbootauth.web.ResponseDto;

/**
 * Created by Sshs0702 on 2023. 3. 23.
 *
 * 인증 시 사용할 Response Util
 *
 */
public class CustomResponseUtil {

    public static final Logger log = LoggerFactory.getLogger(CustomResponseUtil.class);

    public static void success(HttpServletResponse response, Object dto, String message, HttpStatus httpStatus) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            ResponseDto<?> responseDto = new ResponseDto<>(httpStatus.value(), message, dto);

            String responseBody = mapper.writeValueAsString(responseDto);

            response.setContentType("application/json;charset=utf-8");
            response.setStatus(httpStatus.value());
            response.getWriter().println(responseBody);
            log.info("msg: {}, responseBody: {}", message, responseBody);
        } catch (Exception e) {
            throw new CustomApiException(e.getMessage());
        }
    }

    public static void fail(HttpServletResponse response, String message , HttpStatus httpStatus) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            ResponseDto<?> responseDto = new ResponseDto<>(httpStatus.value(), message, null);

            String responseBody = mapper.writeValueAsString(responseDto);

            response.setContentType("application/json;charset=utf-8");
            response.setStatus(httpStatus.value());
            response.getWriter().println(responseBody);
            log.error("msg: {}, responseBody: {}", message, responseBody);
        } catch (Exception e) {
            throw new CustomApiException(e.getMessage());
        }
    }
}
