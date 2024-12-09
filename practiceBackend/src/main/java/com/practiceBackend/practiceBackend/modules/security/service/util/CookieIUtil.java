package com.practiceBackend.practiceBackend.modules.security.service.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;

import java.util.Arrays;

public class CookieIUtil {
    @Value("${server.url}")
    private static String DOMAIN_URL;

    public static ResponseCookie createAccessToken(String accesstoken) {

        //path(String)	쿠키의 경로를 설정. 기본값은 요청 URL의 경로입니다. 예: /
        //domain(String)	쿠키의 도메인을 설정. 특정 서브도메인에서만 쿠키가 유효하도록 설정 가능.
        //maxAge(Duration)	쿠키의 유효기간을 설정. 예: Duration.ofDays(1) → 1일
        //httpOnly(boolean)	true로 설정 시, 클라이언트의 JavaScript에서 쿠키 접근 차단. 보안 강화를 위해 권장됨.
        //secure(boolean)	true로 설정 시, HTTPS 프로토콜에서만 쿠키 전송 가능.
        //sameSite(String)	SameSite 속성을 설정하여 CSRF 공격 방지. 예: Strict, Lax, None.


        return ResponseCookie.from("accessToken", accesstoken)
                .path("/") // 쿠키의 경로 설정
                .maxAge(1800000L)// 유효기간
                .secure(true) // true 이므로 HTtp 프로토콜에서만 쿠키전송가능
                .domain(DOMAIN_URL) // 쿠키가 유효한 도메인
                .httpOnly(true).//
                sameSite("none").build();
    }



//    public String AccessTokenFromHeader(HttpServletRequest request){
//        Cookie[] cookies = request.getCookies();
//        if(cookies != null){
//            for(Cookie cookie : cookies){
//                if(cookie.getName().equals("accessToken")){
//                    return cookie.getValue();
//                }
//            }
//        }
//        return "";
//    }

    public String AccessTokenFromHeader(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            // 쿠키에서 "accessToken"을 찾아 반환
            return Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("accessToken"))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse("");  // 값이 없으면 빈 문자열 반환
        }
        return "";  // 쿠키가 없으면 빈 문자열 반환
    }



//    public String refreshTokenFromHeader(HttpServletRequest request){
//        Cookie[] cookies = request.getCookies();
//        if(cookies != null){
//            for(Cookie cookie : cookies){
//                if(cookie.getName().equals("refreshToken")){
//                    return cookie.getValue();
//                }
//            }
//        }
//        return "";
//    }

    public String refreshTokenFromHeader(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            return Arrays.stream(cookies)
                    .filter(
                            cookie -> cookie.getName().equals("refreshToken")
                    )
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse("");
        }
        return "";
    }

}
