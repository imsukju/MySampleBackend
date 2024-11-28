package com.practiceBackend.practiceBackend.modules.security.service;

import com.practiceBackend.practiceBackend.entity.RefreshToken;
import com.practiceBackend.practiceBackend.entity.User;
import com.practiceBackend.practiceBackend.modules.login.Dto.LoginDTO;
import com.practiceBackend.practiceBackend.modules.login.repository.UserRepository;
import com.practiceBackend.practiceBackend.modules.security.dto.TokenDTO;
import com.practiceBackend.practiceBackend.modules.security.filter.provider.AuthenticProvider;
import com.practiceBackend.practiceBackend.modules.security.filter.provider.TokenProvider;
import com.practiceBackend.practiceBackend.modules.security.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class TokenService {

    TokenProvider tokenProvider;
    RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;


    private final AuthenticProvider authenticProvider;


    public String createAccessToken(LoginDTO user) {
        return tokenProvider.createAccessToken(user);
    }

    public String createRefreshToken(LoginDTO user) {
        String jti = UUID.randomUUID().toString();
        String tokenname = tokenProvider.createRefreshToken(user,jti);

        List<RefreshToken> refreshTokens = refreshTokenRepository.findByUsername(user.getId());
        refreshTokenRepository.deleteByUsername(user.getId());

        RefreshToken rt = RefreshToken.builder()
                .refreshToken(tokenname)
                .username(user.getId())
                .jti(jti)
                .build();

        refreshTokenRepository.save(rt);

        log.info("리프레쉬 토큰 발급완료");
        return tokenname;

    }

    public boolean isValidAccesToken(String token){
        return tokenProvider.isValidAccessToken(token);
    }

    public String finduseridByToken(String token){
        return tokenProvider.finduseridByToken(token);

    }

    public void createAssentication(String accessToken)
    {
        String username = this.finduseridByToken(accessToken);
        User user = userRepository.findByUserid(username).get();
        UsernamePasswordAuthenticationToken a = authenticProvider.authenticate(user);
        SecurityContextHolder.getContext().setAuthentication(a);
    }


}
