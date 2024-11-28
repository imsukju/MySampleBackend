package com.practiceBackend.practiceBackend.modules.security.filter.provider;

import com.practiceBackend.practiceBackend.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthenticProvider {

    public UsernamePasswordAuthenticationToken authenticate(User user) {
        String username = user.getUserid();

        UsernamePasswordAuthenticationToken ua = new UsernamePasswordAuthenticationToken(username,
                null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

        return ua;

    }


}
