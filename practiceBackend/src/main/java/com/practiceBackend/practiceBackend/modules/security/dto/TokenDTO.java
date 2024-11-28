package com.practiceBackend.practiceBackend.modules.security.dto;

import lombok.*;


@Getter
@Setter
public class TokenDTO {
    String accessToken;
    String refreshToken;
}
