package com.widus.springbootauth.jwt;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Sshs0702 on 2021. 4. 27.
 *
 * Jwt Repository
 */
public interface JwtRepository extends JpaRepository<JwtDao, Long> {}
