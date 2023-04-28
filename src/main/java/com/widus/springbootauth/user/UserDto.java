package com.widus.springbootauth.user;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Sshs0702 on 2023. 4. 28.
 *
 * 유저 Dto
 */
@Getter
@Setter
@Builder
public class UserDto {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String token;
    private UserEnum role;

    public UserDao toEntity(UserDto userDto) {
        return UserDao.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .role(userDto.getRole())
                .build();
    }
}
