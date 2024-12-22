package com.practiceBackend.practiceBackend.modules.oAuth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.security.Key;
import java.util.Collection;
import java.util.Date;

public class MyCustomOAuth2Token extends AbstractAuthenticationToken {
    private Key AccessSecretKey;
    private Key RefreshScretKey;

    private final String token;
    private final long AccessTokenValiditySecond = 1800 * 1000;
    private final long RefreshTokenValiditySecond = 3600 * 1000;

    @PostConstruct
    public void init()
    {
        AccessSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        RefreshScretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }


    public String createAccessToken(String username)
    {
        long now = System.currentTimeMillis();

        String AccessToken = Jwts.builder()
                .setSubject(username) // 사용자 이름 or id
                .setIssuedAt(new Date(now)) // 발행시간
                .setExpiration(new Date(now + AccessTokenValiditySecond))
                .signWith(AccessSecretKey) // 서명을 등록
                .compact();
        return AccessToken;
    }
//


    public MyCustomOAuth2Token(Collection<? extends GrantedAuthority> authorities, String token) {
        super(authorities);
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
//