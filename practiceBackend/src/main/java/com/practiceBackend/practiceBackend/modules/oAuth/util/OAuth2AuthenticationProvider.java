package com.practiceBackend.practiceBackend.modules.oAuth.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OAuth2AuthenticationProvider implements AuthenticationProvider {
    //AuthenticationProvider는 보통 Spring Security에서 사용되는 인터페이스로, 애플리케이션의 인증로직을 커스터마이징 하거나 확장 될 떄 사용


    @Override
    //인증 요청을 처리하는 메소드
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //OAuth2LoginAuthenticationToken 는 AbstractAuthenticationToken 확장한거고
        // AbstractAuthenticationToken 는 Authentication의 구현체
        OAuth2LoginAuthenticationToken token = (OAuth2LoginAuthenticationToken) authentication;

        String accessToken = token.getPrincipal().toString();

        try{

        }catch (Exception e){

        }

        return null;
    }

    @Override
    //이 AuthenticationProvider가 특정 인증 유형을 처리할 수 있는지 여부를 반환합니다.
    public boolean supports(Class<?> authentication) {
        //authentication은 실제로는 Authentication 타입이지만,
        // 그 구현체로 다양한 클래스(예: UsernamePasswordAuthenticationToken, OAuth2LoginAuthenticationToken 등)가 존재할 수 있습니다.
        //Class<?>는 유연한 타입 처리를 위해 사용됩니다. 즉, OAuth2LoginAuthenticationToken 같은 특정 구현체를 처리할 수 있는지 확인하려면,
        // Class<?>로 해당 클래스의 타입을 비교합니다.
        return OAuth2LoginAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
