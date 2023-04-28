package com.widus.springbootauth.auth;

import com.widus.springbootauth.web.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sshs0702 on 2021. 4. 27.
 *
 * 인증 컨트롤러
 */
@RequestMapping("/api/user")
@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 회원가입 API
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid AuthReqDto.SignupReqDto signupReqDto, BindingResult bindingResult) {

        // 유효성 검사
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            return new ResponseEntity<>(new ResponseDto<>(-1, "유효성 검사 실패", errorMap), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(authService.signup(signupReqDto));
    }


}
