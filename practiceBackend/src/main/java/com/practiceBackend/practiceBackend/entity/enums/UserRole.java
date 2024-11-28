package com.practiceBackend.practiceBackend.entity.enums;

import lombok.*;

@Getter

public enum UserRole {
    Guest("손님")
    ,Admin("관리자");

    private final String kor;

    UserRole(String kor) {
        this.kor = kor;
    }

}
