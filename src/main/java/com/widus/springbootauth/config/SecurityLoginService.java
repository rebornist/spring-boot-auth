package com.widus.springbootauth.config;

import com.widus.springbootauth.user.UserDao;
import com.widus.springbootauth.user.UserDetail;
import com.widus.springbootauth.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by Sshs0702 on 2021. 4. 21.
 *
 * 로그인 서비스
 *
 */
@Service
public class SecurityLoginService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 시큐리티로 로그인 시, 시큐리티가 loadUserByUsername() 메소드를 호출함
     * 정보가 없을 시 UsernameNotFoundException 발생
     * 정보가 있을 시 정상적으로 시큐리티 컨텍스트 내부 세션에 로그인된 세션이 생성된다.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDao user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InternalAuthenticationServiceException("인증실패"));

        return new UserDetail(user);
    }

}
