package com.practiceBackend.practiceBackend.modules.oAuth.handler;

import com.practiceBackend.practiceBackend.modules.login.Dto.LoginDTO;
import com.practiceBackend.practiceBackend.modules.login.service.LoginService;
import com.practiceBackend.practiceBackend.modules.security.service.CookieService;
import com.practiceBackend.practiceBackend.modules.security.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.Map;

@Component("oAuth2LoginSuccessHandler")
 class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final TokenService tokenService;
    private final LoginService loginService;

    public OAuth2LoginSuccessHandler(TokenService tokenService, LoginService loginService) {
        this.tokenService = tokenService;
        this.loginService = loginService;

    }

    @Value("${client.url}")
    private String clientUrl;

//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
//        cookieService.createCookie(authentication.getPrincipal().toString());
//        response.sendRedirect(clientUrl);
//        chain.doFilter(request, response);
//    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication
    ) throws IOException, ServletException {
        String a = authentication.getPrincipal().toString();
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setId((String) attributes.get("email"));
        String token = null;
        try {
            token = loginService.crewateAccessToken(loginDTO,response);
        } catch (
                LoginException e) {
            throw new RuntimeException(e);
        }


        tokenService.createAthenticationForoAUth(token,(String) attributes.get("email"));
//        SecurityContextHolder.getContext().setAuthentication(token);
        response.sendRedirect(clientUrl);
    }


}
