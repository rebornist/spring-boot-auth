package com.widus.springbootauth.auth;

import com.widus.springbootauth.jwt.JwtDto;
import com.widus.springbootauth.jwt.JwtRepository;
import com.widus.springbootauth.jwt.JwtService;
import com.widus.springbootauth.user.UserDetail;
import com.widus.springbootauth.web.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sshs0702 on 2021. 4. 27.
 *
 * 인증 컨트롤러
 */
@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final JwtRepository jwtRepository;

    /**
     * 회원가입 API
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid AuthReqDto.SignupReqDto signupReqDto, HttpServletRequest request, BindingResult bindingResult) {

        // 유효성 검사
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            return new ResponseEntity<>(new ResponseDto<>(-1, "유효성 검사 실패", errorMap), HttpStatus.BAD_REQUEST);
        }

        // 회원 저장 후 토큰 발급
        UserDetail user = authService.signup(signupReqDto);
        String ip =  request.getRemoteAddr();
        JwtDto refreshTokenDto = jwtService.CreateRefreshToken(user, ip);
        jwtRepository.save(refreshTokenDto.toEntity(refreshTokenDto));

        return ResponseEntity.ok(new AuthRespDto(jwtService.CreateAccessToken(user, ip).getToken()));
    }


}
