package com.widus.springbootauth.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;


/**
 * Created by Sshs0702 on 2023. 3. 23.
 *
 * User Entity
 *
 */
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tb_user")
@Entity
public class UserDao {

    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 유저명
    @Column(length = 45, nullable = false, unique = true)
    private String username;

    // 패스워드
    @Column(length = 100, nullable = false)
    private String password;

    // 이메일
    @Column(length = 100, nullable = false, unique = true)
    private String email;

    // 성명
    @Column(length = 45, nullable = false)
    private String fullname;

    // 권한
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserEnum role;

    // 등록일자
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 변경일자
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // 생성자
    @Builder
    public UserDao(Long id, String username, String password, String email, String fullname,
                   UserEnum role, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullname = fullname;
        this.role = role;
    }

}
