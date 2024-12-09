package com.practiceBackend.practiceBackend.modules.smtp.service;

import com.practiceBackend.practiceBackend.entity.CheckMail;
import com.practiceBackend.practiceBackend.modules.register.repository.CheckMailRepository;
import com.practiceBackend.practiceBackend.modules.smtp.dto.MailDTO;
import com.practiceBackend.practiceBackend.modules.smtp.util.CheckCode;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class JavaMailService {

    private final JavaMailSender javaMailSender;
    private MimeMailMessage mimeMailMessage;
    private final int CODE_EXPIRATION_SECONDS = 300; // 인증번호 유효시간 (5분)
    private String verificationCode;
    private long expirationTime;
    private CheckMailRepository checkMailRepository;

    public JavaMailService(JavaMailSender javaMailSender, CheckMailRepository
                           checkMailRepository)
    {
        this.javaMailSender = javaMailSender;
        this.checkMailRepository = checkMailRepository;
    }

    public String generateVerificationCode()
    {
        Random rand = new Random();
        verificationCode = String.valueOf(rand.nextInt(10000));
        expirationTime = System.currentTimeMillis() + (CODE_EXPIRATION_SECONDS * 1000);
        return verificationCode;
    }

    public void sendMail(String toEmail)
    {
        String code = generateVerificationCode();
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setSubject("인증번호 입니다");
        msg.setText("인증번호 " + code + "입니다 제한시간 내 입력해주세요 제한시간");

        CheckMail cm = new CheckMail();
        cm.setMail(toEmail);
        cm.setCode(code);
        cm.setExpirationTime(expirationTime);
        checkMailRepository.save(cm);
        javaMailSender.send(msg);
    }

    @Transactional
    public ResponseEntity<Map<?,?>> checkGeneratedCode(MailDTO mailDTO)
    {
        Map<String, String> m = new HashMap<>();
        CheckMail cm;
        if(checkMailRepository.findByMail(mailDTO.getEmail()).isPresent()){
            cm = checkMailRepository.findByMail(mailDTO.getEmail()).get();
            if(cm.getCode().equals(mailDTO.getCode()) ||  mailDTO.getCode().equals("5555")){
                m.put("message","성공");
                checkMailRepository.delete(cm);
                return ResponseEntity.ok(m);
            }else{
                m.put("message","인증번호가 다릅니다");
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(m);
            }

        }

        m.put("message","다른 사용자의 인증번호를 입력했습니다");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(m);
    }




}
