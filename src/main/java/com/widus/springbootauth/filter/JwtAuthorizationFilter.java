package com.widus.springbootauth.filter;

import com.widus.springbootauth.jwt.JwtRepository;
import com.widus.springbootauth.jwt.JwtService;
import com.widus.springbootauth.jwt.JwtVo;
import com.widus.springbootauth.user.UserDetail;
import com.widus.springbootauth.user.UserDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Sshs0702 on 2023. 4. 21.
 * 
 * JWT 인가 필터
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private JwtRepository jwtRepository;
    private JwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtService jwtService, JwtRepository jwtRepository) {
        super(authenticationManager);
        this.jwtRepository = jwtRepository;
        this.jwtService = jwtService;
    }

    // JWT 토큰 헤더를 추가하지 않더라도 해당 필터는 통과할 수 있지만, 결국 시큐리티에서 세션값 검증에 실패하게 됨
    // 따라서, JWT 토큰 헤더가 없는 경우에는 401 에러를 반환하도록 처리
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (isHeaderVerify(request, response)) {

            // 토큰이 존재함
            String token = request.getHeader(JwtVo.HEADER).replace(JwtVo.TOKEN_PREFIX, "");
            UserDto userDto = jwtService.VerifyAccessToken(token, request.getRemoteAddr());

            // 기존 토큰과 다른 경우
            if (!token.equals(userDto.getToken())) {
                response.addHeader(JwtVo.HEADER, userDto.getToken());
            }

            UserDetail user = new UserDetail(userDto.toEntity(userDto));

            // 임시 세션 생성(UserDetails or username)
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
                                                                                    user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    private boolean isHeaderVerify(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader(JwtVo.HEADER);
        if (header == null || !header.startsWith(JwtVo.TOKEN_PREFIX)) {
            return false;
        }
        return true;
    }


}
