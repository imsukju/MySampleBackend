package com.practiceBackend.practiceBackend.modules.register;


import com.practiceBackend.practiceBackend.modules.register.DTO.RegisterDtoRequest;
import com.practiceBackend.practiceBackend.modules.register.service.RegisterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@Slf4j
public class RegisterController {
    private final RegisterService registerService;


    @PostMapping("/registers")
    // requestForm.append("data", new Blob([JSON.stringify(jsonData)], { type: "application/json" }));
    // JSON 데이터를 Blob 형태로 변환하고 MIME 타입을 "application/json"으로 설정하여
    // 이를 "data"라는 이름으로 FormData 객체에 추가함
    //MIME 타입(Multipurpose Internet Mail Extensions)은 파일 형식이나 데이터 형식을 나타내는 표준화된 문자열입니다.
    public ResponseEntity<Map> register(@RequestPart("data") RegisterDtoRequest user,
    @RequestPart("image") MultipartFile profileImage) throws IOException {

        registerService.register(user, profileImage);
        log.info("Register successful");

        Map<String, String> response = new HashMap<>();
        response.put("message", "회원가입완료");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{email}/{id}")
    public ResponseEntity<Map<?,?>>  whatisthis(@PathVariable String email, @PathVariable String id)
    {
        ResponseEntity<Map<?,?>> message =  registerService.duplicateIdCheck(id);

        return message;

    }
}
