package com.practiceBackend.practiceBackend.modules.login;

import com.practiceBackend.practiceBackend.entity.User;
import com.practiceBackend.practiceBackend.modules.login.Dto.DataDTOResponse;
import com.practiceBackend.practiceBackend.modules.login.Dto.LoginDTO;
import com.practiceBackend.practiceBackend.modules.login.Dto.massage.ResponeMassage;
import com.practiceBackend.practiceBackend.modules.login.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.practiceBackend.practiceBackend.modules.login.Dto.DataDTOResponse.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
public class LoginController {
    private final LoginService loginService;


    @PostMapping("/logins")
    public ResponseEntity<ResponeMassage> validaedLogin(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {



        return loginService.ValidateLogin(loginDTO, response);
    }

//    @GetMapping({"/users"})
//    public ResponseEntity<?> findUserByToken(@CookieValue("accessToken") String accessToken) {
//        DataDTOResponse dr = new DataDTOResponse();
//        DataDTO data = loginService.finduseridBytoken(accessToken);
//        dr.setData(data);
//
//        // 디버깅: 반환된 데이터 확인
//        if (data == null) {
//            System.out.println("User data is null for token: " + accessToken);
//        } else {
//            System.out.println("User Data:");
//            System.out.println("ID: " + data.getId());
//            System.out.println("Options: " + data.getOptions());
//            System.out.println("ProfileImage: " + data.getProfileImage());
//        }
//
//
//        return ResponseEntity.ok(dr);
//
//
//    }

    @GetMapping({"/users"})
    public ResponseEntity<?> usersController(@CookieValue("accessToken") String accessToken) {


        DataDTOResponse dr = new DataDTOResponse();
        DataDTO data = loginService.finduseridBytoken(accessToken);
        dr.setData(data);

        Map<String,DataDTO> map = new HashMap<>();

        map.put("data",data);



        return ResponseEntity.ok(map);


    }

    @PostMapping(value = "/user/logout")
    public ResponseEntity<ResponeMassage> logoutController(HttpServletResponse response,@CookieValue("accessToken") String accessToken) {
        loginService.logout(response);
        return null;
    }


}
