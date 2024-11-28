package com.practiceBackend.practiceBackend.modules.smtp.dto;

import lombok.*;

@Getter
@Setter
public class MailDTO {
    private String email;
    private String code;

    @Getter
    @Setter
    public static class OnlyMailDTO
    {
        private String email;
    }
}
