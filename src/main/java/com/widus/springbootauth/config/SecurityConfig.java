package com.widus.springbootauth.config;

import com.widus.springbootauth.user.UserEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Created by Widus on 2023. 3. 23..
 *
 * Security 설정
 * JWT 인증을 위한 설정
 *
 */
@Configuration
public class SecurityConfig {

    // TODO: JWT 인증을 위한 설정
    // Log 세팅
    private  final Logger log = LoggerFactory.getLogger(this.getClass());

    // Bcrypt 암호화를 위한 설정
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        log.debug("BCryptPasswordEncoder" + " 빈등록 완료!");
        return new BCryptPasswordEncoder();
    }

    // JWT 필터 등록에 필요한 설정
    public class CustomSerurityFilterManager extends AbstractHttpConfigurer<CustomSerurityFilterManager, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http.addFilter(new JwtAuthenticationFilter(authenticationManager));
            http.addFilter(new JwtAuthorizationFilter(authenticationManager));
            super.configure(http);
            log.debug("디버그 : CustomSecurityFilterManager 빈 등록됨");
        }
    }
    
    // JWT 필터 등록
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        // iframe 사용 방지를 위한 설정
        http.headers().frameOptions().disable();

        // csrf 사용 방지를 위한 설정
        http.csrf().disable();
        
        // cors 사용 권한 부여를 위한 설정
        http.cors().configurationSource(corsConfigurationSource());

        // React 사용을 위해 폼로그인 사용 안함
        http.formLogin().disable();

        // httpBasic 사용 안함
        // httpBasic 사용 시 브라우저가 팝업창을 이용하여 사용자 인증을 진행함
        http.httpBasic().disable();

        // JWT 필터 등록
        http.apply(new CustomSerurityFilterManager());

        // 인증 실패 시 401 에러를 리턴

        // 권한 확인 실패 시 403 에러를 리턴

        // Request 요청 시 권한에 따른 접근 권한 부여
        http.authorizeRequests()
                .antMatchers("/api/v1/auth/**").permitAll()
                .antMatchers("/api/v1/user/**").hasRole(UserEnum.USER.getValue())
                .antMatchers("/api/v1/manager/**").hasRole(UserEnum.MANAGER.getValue())
                .anyRequest().authenticated();

        return http.build();
    }
    
    // cors 사용 권한 설정
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE, OPTIONS 등 모든 요청 허용
        configuration.addAllowedOriginPattern("*"); // 모든 IP 허용(프론트앤드만 허용 react, vue, angular 등)
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
