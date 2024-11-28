package com.practiceBackend.practiceBackend.modules.security.filter.provider;

import com.practiceBackend.practiceBackend.entity.RefreshToken;
import com.practiceBackend.practiceBackend.modules.login.Dto.LoginDTO;
import com.practiceBackend.practiceBackend.modules.security.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
@Transactional
public class TokenProvider {

    private final RefreshTokenRepository refreshTokenRepository;
    private final long AccessTokenValiditySecond = 1800 * 1000;
    private final long RefreshTokenValiditySecond = 3600 * 1000;
    private Key AccessSecretKey;
    private Key RefreshScretKey;

    public TokenProvider(RefreshTokenRepository refreshTokenRepository){
        this.refreshTokenRepository = refreshTokenRepository;
    }


    @PostConstruct
    public void init()
    {
        AccessSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        RefreshScretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }
    //Java의 jjwt 라이브러리를 사용할 때, 페이로드는 Jwts.builder() 메서드를 통해 추가되므로


    //AccessToken에 필요한 데이터 필수인거
    //사용자 식별 정보
    //사용자 ID 또는 이름 (ex: sub, userId).
    //이메일 또는 기타 인증 가능한 정보.
    //유효성 정보
    //토큰 발급 시간 (iat).
    //토큰 만료 시간 (exp).
    //발급자 정보 (iss).
    //사용 권한
    //역할 또는 권한 (ex: roles, scopes).

    //선택적 정보
    //추가 클레임
    //API의 사용 범위 (scope).
    //요청 IP, 디바이스 정보 등 보안용 데이터.
    //JWT ID
    //토큰의 고유 식별자 (jti)로 중복 검사를 가능하게 함.
    public String createAccessToken(LoginDTO loginDTO)
    {
        long now = System.currentTimeMillis();

        String AccessToken = Jwts.builder()
                .setSubject(loginDTO.getId()) // 사용자 이름 or id
                .setIssuedAt(new Date(now)) // 발행시간
                .setExpiration(new Date(now + AccessTokenValiditySecond))
                .signWith(AccessSecretKey) // 서명을 등록
                .compact();


        return AccessToken;
    }

    public String reCreateAccessToken(String usernid)
    {
        long now = System.currentTimeMillis();

        String AccessToken = Jwts.builder()
                .setSubject(usernid) // 사용자 이름 or id
                .setIssuedAt(new Date(now)) // 발행시간
                .setExpiration(new Date(now + AccessTokenValiditySecond))
                .signWith(AccessSecretKey) // 서명을 등록
                .compact();


        return AccessToken;
    }


    public String createRefreshToken(LoginDTO user,String jti){
        long now = System.currentTimeMillis();



        //. RefreshToken 설계 시 필수 항목
        //1) 필수 정보
        //유저 식별자 (sub): 사용자 고유 ID 또는 식별자.
        //발행자 정보 (iss): 토큰을 발행한 서버 정보.
        //유효기간 (exp): RefreshToken의 만료 시각.
        //토큰 고유 식별자 (jti): 중복 발급 방지 및 추적을 위한 고유 식별값.
        String refreshToken = Jwts.builder()

                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + RefreshTokenValiditySecond))
                .setId(jti)
                .signWith(RefreshScretKey)
                .compact();

        return refreshToken;
    }

    public String generateJti() {
        return UUID.randomUUID().toString();
    }

    public boolean isValidAccessToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(AccessSecretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // 만료 시간 확인
            if (claims.getExpiration().before(new Date())) {

                log.info("Access token expired , Access Token 재발급을 시도합니다");
                String userid = this.finduseridByToken(token);
                isValidRefreshTokenAndReCreateAccesssToken(userid);
                return false;
            }
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Expired token: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported token: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Malformed token: " + e.getMessage());
        } catch (SignatureException e) {
            System.out.println("Invalid signature: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Empty or null token: " + e.getMessage());
        }
        return false;
    }

    public void isValidRefreshTokenAndReCreateAccesssToken(String userid) {

        try{

            if(refreshTokenRepository.findOneByUsername(userid).isPresent()){
                RefreshToken refreshToken = refreshTokenRepository.findOneByUsername(userid).get();

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(RefreshScretKey)
                        .build()
                        .parseClaimsJws(refreshToken.getRefreshToken())
                        .getBody();
                if(claims.getExpiration().before(new Date())){
                    log.info("리프래시 토큰 유효기간 만료");
                    refreshTokenRepository.deleteByUsername(userid);
                }else{
                    this.reCreateAccessToken(userid);
                    log.info("리프레시 토큰으로 AccessToken 재발급을 완료하였습니다");
                }


            }

        } catch (ExpiredJwtException e) {
            System.out.println("Expired token: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported token: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Malformed token: " + e.getMessage());
        } catch (SignatureException e) {
            System.out.println("Invalid signature: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Empty or null token: " + e.getMessage());
        }
    }

    public String finduseridByToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(AccessSecretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
    @Transactional
    public String creteAccessTokenByRefreshToken(String refreshToken,LoginDTO loginDTO) {
        String token;
        try{
            if(refreshTokenRepository.findByRefreshToken(refreshToken).isPresent()){
                Claims claims = Jwts.parserBuilder().setSigningKey(RefreshScretKey).build().parseClaimsJws(refreshToken).getBody();
                if(claims.getExpiration().before(new Date())){
                    RefreshToken r = refreshTokenRepository.findByRefreshToken(refreshToken).get();
                    refreshTokenRepository.delete(r);
                    throw new ExpiredJwtException(null, claims, "Token has expired");
                }else{
                    token = this.createAccessToken(loginDTO);
                    return token;
                }
            }



        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



}
