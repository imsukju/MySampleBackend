package com.practiceBackend.practiceBackend.modules.login.Dto;

import lombok.*;

@Getter
@Setter
public class DataDTOResponse {
    DataDTO data;

    @Getter
    @Setter
    public static class DataDTO
    {
        String id;
        String options;
        String profileImage;
    }
}
