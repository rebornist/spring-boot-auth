package com.widus.springbootauth.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Widus on 2023. 3. 23..
 *
 * User DB 연동을 위한 Repository
 *
 */
public interface UserRepository extends JpaRepository<UserDao, Long> {

    public Optional<UserDao> findByUsername(String username);

}
