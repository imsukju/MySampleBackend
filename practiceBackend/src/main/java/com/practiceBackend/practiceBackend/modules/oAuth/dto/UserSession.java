package com.practiceBackend.practiceBackend.modules.oAuth.dto;

import com.practiceBackend.practiceBackend.entity.User;

public class UserSession {

    private final String email;

    private UserSession(User user) {
        this.email = user.getUserid();
    }

    public static UserSession of(User user) {
        return new UserSession(user);
    }

    public String getId() {
        return email;
    }
}
