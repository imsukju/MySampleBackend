package com.practiceBackend.practiceBackend.modules.smtp.util;

import jakarta.mail.Transport;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.InputStream;

public class CustomJavaMailSenderImpl implements JavaMailSender {

    //MimeMessage: 이메일 메시지의 다양한 콘텐츠(텍스트, HTML, 첨부 파일 등)를 지원하는 클래스입니다.


    //이 메소드는 이메일 내용을 작성하기 전에 MimeMessage를 초기화하는 데 사용됩니다.
    //반환된 MimeMessage 객체는 필요한 내용(수신자, 제목, 본문 등)을 추가하여 이메일을 구성합니다.
    @Override
    public MimeMessage createMimeMessage() {
        return null;
    }


    //역할: 주어진 InputStream을 사용해 MimeMessage를 생성합니다.
    //용도: 이메일 메시지가 외부 데이터로부터 로드되는 경우 사용됩니다.
    //예: 저장된 이메일 템플릿이나 파일로부터 이메일을 생성할 때.
    //InputStream을 파싱하여 MimeMessage로 변환합니다.
    //contentStream에 이메일 헤더, 본문 등이 포함되어 있어야 합니다.
    @Override
    public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
        return null;
    }

    //역할: 하나 이상의 MimeMessage 객체를 이메일로 전송합니다.
    //입력: MimeMessage 객체 배열.
    //여러 개의 이메일을 한 번에 전송할 때 사용됩니다.
    //메일 서버로 이메일을 보내는 역할을 합니다.
    //예외: 전송 실패 시 MailException을 던집니다.
    @Override
    public void send(MimeMessage... mimeMessages) throws MailException {

    }

    //역할: 하나 이상의 SimpleMailMessage 객체를 이메일로 전송합니다.

    //SimpleMailMessage: MimeMessage에 비해 간단한 텍스트 기반 이메일을 작성하는 클래스입니다.
    //HTML, 첨부 파일 등을 지원하지 않습니다.
    //입력: SimpleMailMessage 객체 배열.
    //여러 개의 텍스트 이메일을 한 번에 전송할 때 사용됩니다.
    //메일 전송에 성공하거나 실패할 경우 MailException을 던질 수 있습니다.

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {
        for(SimpleMailMessage simpleMailMessage : simpleMessages)
        {
            MimeMessage mimeMessage = createMimeMessage();
        }

    }
}
