package com.practiceBackend.practiceBackend.modules.security.service.util;

import org.springframework.security.core.GrantedAuthority;

public class MyAuthorities implements GrantedAuthority {
    @Override
    public String getAuthority() {
        return "";
    }
}
