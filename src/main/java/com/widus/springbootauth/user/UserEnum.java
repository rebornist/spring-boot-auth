package com.widus.springbootauth.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Sshs0702 on 2023. 3. 23.
 *
 * User 권한 종류
 *
 */
@AllArgsConstructor
@Getter
public enum UserEnum {
    ADMIN("소유자"), MANAGER("관리자"), VISITOR("방문자"), USER("사용자");
    private String value;
}
