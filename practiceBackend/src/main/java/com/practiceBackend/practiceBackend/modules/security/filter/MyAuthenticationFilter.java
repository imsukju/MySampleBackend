package com.practiceBackend.practiceBackend.modules.security.filter;

import com.practiceBackend.practiceBackend.entity.User;
import com.practiceBackend.practiceBackend.modules.login.repository.UserRepository;
import com.practiceBackend.practiceBackend.modules.security.filter.provider.AuthenticProvider;
import com.practiceBackend.practiceBackend.modules.security.service.CookieService;
import com.practiceBackend.practiceBackend.modules.security.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@AllArgsConstructor
@Slf4j
public class MyAuthenticationFilter extends GenericFilterBean{
    private CookieService cookieService;
    private TokenService tokenService;
//    private AuthenticationManager authenticationManager;



    //Query Parameters: URL에 포함된 쿼리 문자열 (예: ?key=value).
    //Form Data: POST 요청에서 폼 데이터로 전송된 값.
    //Headers: 요청과 함께 전달된 헤더 정보.
    //Cookies: 요청 시 전달된 쿠키 값.
    //Body: 요청 본문(주로 JSON, XML, 등).


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest pathtem = (HttpServletRequest) request;
        String path = pathtem.getRequestURI();







        //
        log.info("HTTP Method: {}", pathtem.getMethod());
        log.info("현재경로" + pathtem.getRequestURI());
          if (path.startsWith("/registers") || path.startsWith("/mail/") || path.startsWith("/logins") || path.startsWith("/mail/check")
                || path.startsWith("/mail/send") || path.startsWith("/users/")  ) {
            chain.doFilter(request, response); // 다음 필터로 전달
            return;
        }



        //SecurityContextHolder란?
        //SecurityContextHolder는 Spring Security에서 현재 애플리케이션의 인증(Authentication) 및 권한(Authorization) 정보를 저장하고 관리하는 핵심 클래스입니다.
        // 필터체인 마다 초기화가 되므로 여기다 설정했음
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String accessToken = cookieService.AccessTokkenFromHeader(httpRequest);

        tokenService.createAssentication(accessToken); // 인증을 만들어서 넣음

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
                log.warn("SecurityContextHolder에 인증 정보가 없습니다.");
        } else {
            log.info("Authenticated User: {}", auth.getName());
            log.info("Authorities: {}", auth.getAuthorities());
        }

        if(tokenService.isValidAccesToken(accessToken)){
            chain.doFilter(request, response);
            return;
        }else {
            log.info("에러 던지는 경로" + pathtem.getRequestURI());
            throw new ServletException("Invalid access token");
        }


    }
}
