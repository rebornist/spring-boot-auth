package com.widus.springbootauth.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.widus.springbootauth.user.UserDetail;
import com.widus.springbootauth.user.UserReqDto;
import com.widus.springbootauth.user.UserRespDto;
import com.widus.springbootauth.util.CustomResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Created by Sshs0702 on 2023. 3. 23.
 *
 * JWT 인증 필터
 */

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    // 인증 관리자 등록
    private AuthenticationManager authenticationManager;
    
    // JWT 인증 필더 생성
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // 인증 시도
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {

            // username, password 파라미터를 받아서 인증 시도
            ObjectMapper mapper = new ObjectMapper();
            UserReqDto.LoginReqDto user = mapper.readValue(request.getInputStream(), UserReqDto.LoginReqDto.class);

            // 인증 관리자에게 인증 시도
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            // 인증 성공 시 인증 객체 반환
            // JWT 사용 시에도 시큐리티의 권한 체크, 인증 체크 도움을 받을 수 있도록 세션을 생성
            // 해당 세션의 유효기간은 request, response가 끝날 때 까지
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return authentication;

        } catch (Exception e) {
            e.printStackTrace();

            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    // 인증 성공시 처리 로직
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                Authentication authResult) throws IOException, ServletException {
        UserDetail loginUser = (UserDetail) authResult.getPrincipal();
        String jwtToken = JwtProcess.createToken(loginUser);
        response.addHeader(JwtVo.HEADER, jwtToken);

        UserRespDto.SigninRespDto loginRespDto = new UserRespDto.SigninRespDto(loginUser.getUser());
        CustomResponseUtil.success(response, loginRespDto);
    };

    // 인증 실패시 처리 로직
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                              AuthenticationException failed) throws IOException, ServletException {
        CustomResponseUtil.fail(response, "인증 실패", HttpStatus.UNAUTHORIZED);
    };



}
