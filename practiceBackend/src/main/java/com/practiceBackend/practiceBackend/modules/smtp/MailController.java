package com.practiceBackend.practiceBackend.modules.smtp;

import com.amazonaws.Response;
import com.practiceBackend.practiceBackend.modules.register.repository.CheckMailRepository;
import com.practiceBackend.practiceBackend.modules.smtp.dto.MailDTO;
import com.practiceBackend.practiceBackend.modules.smtp.service.JavaMailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;
import java.util.Map;

@RestController
@AllArgsConstructor
@Slf4j
public class MailController {
    private final JavaMailService javaMailService;
    private final CheckMailRepository checkMailRepository;

    @PostMapping("/mail/send")
    public void maniSend(@RequestBody MailDTO.OnlyMailDTO mailDTO)
    {
        javaMailService.sendMail(mailDTO.getEmail());
        log.info("메일이 전송되었습니다");
        
    }

    @PostMapping("/mail/check")
    public ResponseEntity<Map<?,?>> mailCheck(@RequestBody MailDTO mailDTO)
    {
        return javaMailService.checkGeneratedCode(mailDTO);
    }


}
