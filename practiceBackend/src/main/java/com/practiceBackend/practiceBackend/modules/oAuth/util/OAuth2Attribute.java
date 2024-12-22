package com.practiceBackend.practiceBackend.modules.oAuth.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;
import java.util.Map;
@RequiredArgsConstructor
public class OAuth2Attribute {
    // oAuth서버에서 받은 어트리뷰트를 처리하기위해 만든 클래스
    //DefaultUserinfo 는 Attribute 맵을 제공하길 원홤


    public static Map<String,Object> getAttributes(String where, String attributename,  Map<String, Object> attributes) {
        Map<String,Object> attributesz = new HashMap<>();
        if (where == null || attributename == null || attributes == null) {
            throw new IllegalArgumentException("Invalid input: where, attributename, and attributes must not be null.");
        }
        // 아직은 oAuth를 구글밖에 안햇으므로 하나만있음
        if(where.equals("google")){
            attributesz = attributes;
        }
        return attributes;
    }


}
