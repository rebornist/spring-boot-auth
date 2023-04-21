package com.widus.springbootauth.user;

import com.widus.springbootauth.web.ResponseDto;
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

@RequestMapping("/api/user")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid UserReqDto.SignupReqDto signupReqDto, BindingResult bindingResult) {

        // 유효성 검사
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            return new ResponseEntity<>(new ResponseDto<>(-1, "유효성 검사 실패", errorMap), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(userService.signup(signupReqDto));
    }

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody @Valid UserReqDto.SigninReqDto signinReqDto, BindingResult bindingResult) {
        // 유효성 검사
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            return new ResponseEntity<>(new ResponseDto<>(-1, "유효성 검사 실패", errorMap), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(userService.signin(signinReqDto));
    }


}
