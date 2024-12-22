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
        //credentials 는 인증을 위해 제공된 정보를 의미
        UsernamePasswordAuthenticationToken ua = new UsernamePasswordAuthenticationToken(username,
                null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

        return ua;

    }

    public UsernamePasswordAuthenticationToken authenticateForoAuth(String username) {
        //credentials 는 인증을 위해 제공된 정보를 의미
        UsernamePasswordAuthenticationToken ua = new UsernamePasswordAuthenticationToken(username,
                null, List.of(new SimpleGrantedAuthority("ROLE_USER")));

        return ua;

    }


}
