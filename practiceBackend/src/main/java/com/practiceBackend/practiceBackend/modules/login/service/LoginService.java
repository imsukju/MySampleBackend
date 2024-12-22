package com.practiceBackend.practiceBackend.modules.login.service;

import com.practiceBackend.practiceBackend.entity.User;
import com.practiceBackend.practiceBackend.modules.login.Dto.DataDTOResponse;
import com.practiceBackend.practiceBackend.modules.login.Dto.LoginDTO;
import com.practiceBackend.practiceBackend.modules.login.Dto.massage.ResponeMassage;
import com.practiceBackend.practiceBackend.modules.login.repository.UserRepository;
import com.practiceBackend.practiceBackend.modules.security.service.CookieService;
import com.practiceBackend.practiceBackend.modules.security.service.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.practiceBackend.practiceBackend.modules.login.Dto.DataDTOResponse.*;

import javax.security.auth.login.LoginException;

@Service
@AllArgsConstructor
@Slf4j
public class LoginService {

    private final TokenService tokenService;
    private final BCryptPasswordEncoder passwordEncoder;


    private final UserRepository userRepository;
    private final CookieService cookieService;


    public ResponseEntity<ResponeMassage> ValidateLogin(LoginDTO loginDTO, HttpServletResponse response)   {

        try{
            if(userRepository.findByUserid(loginDTO.getId()).isPresent()){
                User user = userRepository.findByUserid(loginDTO.getId()).get();
                if(passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
                    String accessToken = this.crewateAccessToken(loginDTO, response);
                    String refreshToken = this.createRefreshToken(loginDTO, response);


                    return  ResponseEntity.ok(new ResponeMassage("로그인 성공하셨습니다"));
                }else {
                    return  ResponseEntity.badRequest().body(new ResponeMassage("비번 틀림"));
                }

            }else{
                log.info("로그인 실패");
                return ResponseEntity.badRequest().body(new ResponeMassage("id 틀림 틀림"));

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public String crewateAccessToken(LoginDTO loginDTO, HttpServletResponse response) throws LoginException {
        String token = tokenService.createAccessToken(loginDTO);
        Cookie cookie = new Cookie("accessToken", token);
        cookie.setHttpOnly(true);    // 쿠키를 클라이언트에서 접근하지 못하도록 제한
        cookie.setSecure(false);     // HTTPS 환경에서만 쿠키 전송
        cookie.setPath("/");        // 모든 경로에서 쿠키 접근 가능
        cookie.setMaxAge(60 * 60 * 24);   // 쿠키의 만료 시간 설정
        response.addCookie(cookie);
       return token;

    }

    public String createRefreshToken(LoginDTO loginDTO, HttpServletResponse response) throws LoginException {
        String token = tokenService.createRefreshToken(loginDTO);
        Cookie cookie = new Cookie("refreshToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24*7);
        response.addCookie(cookie);
        return token;

    }

    public DataDTO finduseridBytoken(String token){
        String userid;
        User user;
        DataDTO dto = new DataDTO();
        try{
            userid = tokenService.finduseridByToken(token);
            if(userid != null){
                user = userRepository.findByUserid(userid).get();
                dto.setId(user.getUserid());
                dto.setProfileImage(user.getProfileImage());
                dto.setOptions("being");
                return dto;
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public void logout(HttpServletResponse response){
        cookieService.deleteCookie(response,null);

    }


}
