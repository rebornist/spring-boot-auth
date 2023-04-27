package com.widus.springbootauth.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by Sshs0702 on 2023. 3. 23.
 *
 * User DB 연동을 위한 Repository
 *
 */

public interface UserRepository extends JpaRepository<UserDao, Long> {
    public Optional<UserDao> findByUsername(String username);

}
