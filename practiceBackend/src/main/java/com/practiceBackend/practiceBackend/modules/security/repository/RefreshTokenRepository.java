package com.practiceBackend.practiceBackend.modules.security.repository;

import com.practiceBackend.practiceBackend.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    public Optional<RefreshToken> findByRefreshToken(String refreshToken);
    public List<RefreshToken> findByUsername(String username);
    public Optional<RefreshToken> findOneByUsername(String username);
    void deleteByUsername(String username);
}
