package com.practiceBackend.practiceBackend.modules.register.repository;

import com.practiceBackend.practiceBackend.entity.CheckMail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface CheckMailRepository extends JpaRepository<CheckMail, Long> {
    Optional<CheckMail> findByMail(String mail);
}
