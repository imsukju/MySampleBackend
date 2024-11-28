package com.practiceBackend.practiceBackend.modules.login.repository;

import com.practiceBackend.practiceBackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserid(String userid);

}
