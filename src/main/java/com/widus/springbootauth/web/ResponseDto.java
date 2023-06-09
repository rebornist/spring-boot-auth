package com.widus.springbootauth.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by Sshs0702 on 2023. 3. 23.
 *
 * 공통 응답 Dto
 *
 */
@RequiredArgsConstructor
@Getter
public class ResponseDto<T> {
    private final int status;
    private final String message;
    private final T data;
}
