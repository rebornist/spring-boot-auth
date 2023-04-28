package com.widus.springbootauth.jwt;

import com.widus.springbootauth.user.UserEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by Sshs0702 on 2021. 4. 27.
 *
 * Jwt Dao
 */
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tb_jwt")
@Entity
public class JwtDao {
    
    // 유저명
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // IP
    @Column(length = 50, nullable = false)
    private String ip;
    
    // 권한
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserEnum role;

    // 토큰
    @Column(length = 1000, nullable = true)
    private String token;

    // 토큰 만료일자
    @Column(nullable = false)
    private Date expiresAt;

    // 등록일자
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public JwtDao(Long id, String ip, UserEnum role, String token, Date expiresAt, LocalDateTime createdAt) {
        this.id = id;
        this.ip = ip;
        this.role = role;
        this.token = token;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

}
