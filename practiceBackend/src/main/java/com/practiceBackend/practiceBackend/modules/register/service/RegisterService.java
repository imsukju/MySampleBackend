package com.practiceBackend.practiceBackend.modules.register.service;

import com.practiceBackend.practiceBackend.entity.User;
import com.practiceBackend.practiceBackend.modules.login.repository.UserRepository;
import com.practiceBackend.practiceBackend.modules.register.DTO.RegisterDtoRequest;
import com.practiceBackend.practiceBackend.modules.register.repository.CheckMailRepository;
import com.practiceBackend.practiceBackend.modules.s3.service.S3Service;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


@Service
@AllArgsConstructor
@Slf4j
public class RegisterService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final S3Service s3Service;
    private final CheckMailRepository checkMailRepository;
    @Transactional
    public User register(RegisterDtoRequest loginDtoRequest, MultipartFile multipartFile) {
        String userid = loginDtoRequest.getId();
        User user = new User();
        try {
            String profileimage = s3Service.uploadFileToS3(multipartFile);
            user = User.builder().
                    userid(loginDtoRequest.getId()).
                    password(passwordEncoder.encode(loginDtoRequest.getPassword())).
                    profileImage(profileimage).build();


            userRepository.save(user);


        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
        return user;
    }

    public ResponseEntity<Map<?,?>> duplicateIdCheck(String id){
        Map<String, String> hello = new HashMap<>();

        if(userRepository.findByUserid(id).isPresent()){
            hello.put("message", "이미 존재하는 id입니다");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(hello);
        }else {
            hello.put("message","완료");
            return ResponseEntity.status(HttpStatus.OK).body(hello);
        }

    }
}
