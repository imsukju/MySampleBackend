package com.practiceBackend.practiceBackend.modules.oAuth.service;



import com.practiceBackend.practiceBackend.modules.oAuth.util.OAuth2Attribute;
import com.practiceBackend.practiceBackend.modules.security.filter.provider.TokenProvider;
import jakarta.servlet.http.HttpSession;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

//Spring Security는 기본적으로 DefaultOAuth2UserService를 사용하여 사용자 정보를 로드합니다.
// 하지만 **사용자 정의 OAuth2UserService**를 등록하면, Spring Security는 이를 대신 호출합니다.
//사실 OAuth2UserService를 실행하는 이유는, OAuth2 인증 후에 사용자 정보를 처리하려는 목적 때문입니다
//*****************************************************
// 결론적으로는 사용자 정보를 가져오기위해 처리하기위해******
//*****************************************************
@Slf4j
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
@Service                                                   //이 클래스는 OAuth2 제공자로부터 사용자 정보를 받아오고, 애플리케이션에 필요한 형식으로 변환하는 역할을 합니다.
public class OAuth2AuthenticationService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final TokenProvider jwtTokenProvider;

    //httpSession 메소드중 하나임
    //void setAttribute(String name, Object value)
    //name: 세션에 저장할 데이터의 키 (문자열).
    //value: 세션에 저장할 데이터의 값 (객체).
    private final HttpSession httpSession;

    //이 메소드는 실제로 사용자의 정보를 로드하는 기능을 합니다.
    // OAuth2UserRequest를 사용하여 제공자로부터 사용자 정보를 받아오고, 이를 OAuth2User 객체로 변환하여 반환합니다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        String attribute = userRequest.getClientRegistration()
                .getProviderDetails()//이 객체는 OAuth2 공급자의 세부 정보를 포함합니다.
                .getUserInfoEndpoint()//이 객체는 사용자 정보(UserInfo) 엔드포인트와 관련된 정보를 포함합니다.
                .getUserNameAttributeName();// 이메서드는 Spring Security가 사용자 이름 속성으로 사용할 OAuth2 사용자 정보의 속성 키를 반환합니다.
        Map<?,?> A = this.attributeMaker(userRequest);

        OAuth2User ou = new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),this.attributeMaker(userRequest),attribute);

        return  ou;
    }

    public Map<String,Object> attributeMaker(OAuth2UserRequest userRequest){
        //사용자 정보 가져오기
        String where = userRequest.getClientRegistration().getRegistrationId();
        String nameAttributeKey = resolveNameAttributeKey(where);

        OAuth2UserService<OAuth2UserRequest,OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User temp = delegate.loadUser(userRequest);
        Map<String, Object> attributes = temp.getAttributes();

        // 필요한 키가 없으면 추가
        if (!attributes.containsKey(nameAttributeKey)) {
            log.info("!attributes.containsKey(nameAttributeKey) : " + !attributes.containsKey(nameAttributeKey));
            attributes.put(nameAttributeKey, where);// 필요한 키 추가
        }


        return OAuth2Attribute.getAttributes(where,nameAttributeKey,attributes);
    }

    //
    public String resolveNameAttributeKey(String provider) {
        switch (provider.toLowerCase()) {
            case "google":
                return "sub"; // Google은 "sub"이 고유 식별자
            case "github":
                return "id";  // GitHub은 "id"가 고유 식별자
            default:
                throw new IllegalArgumentException("Unsupported provider: " + provider);
        }
    }


}
//주어진 코드는 OAuth2 클라이언트에서 사용자 정보를 처리하는 데 사용되는 한 줄의 Java 코드입니다. 이 코드는 Spring Security의 OAuth2 클라이언트를 사용해 구현된 것으로 보입니다. 이제 이 코드의 각 부분을 세부적으로 분석하겠습니다.

//### **1. `userRequest`**
//`userRequest`는 **OAuth2 클라이언트가 생성한 요청 객체**로, 일반적으로 `OAuth2UserRequest` 타입입니다.
//이 객체는 다음과 같은 정보를 포함합니다:
//- OAuth2 클라이언트 등록 정보 (`ClientRegistration`)
//- 엑세스 토큰 (`OAuth2AccessToken`)

//### **2. `getClientRegistration()`**
//`userRequest.getClientRegistration()`은 **클라이언트 등록 정보**를 가져옵니다.
//- 반환 타입은 `ClientRegistration`입니다.
//- 이 객체는 OAuth2 공급자(Google, GitHub 등)에 대한 메타데이터를 포함하고 있습니다.
//  - 클라이언트 ID (`clientId`)
//  - 클라이언트 시크릿 (`clientSecret`)
//  - 인증 서버 URL
//  - 사용자 정보 엔드포인트 등

//### **3. `getProviderDetails()`**
//`userRequest.getClientRegistration().getProviderDetails()`는 **OAuth2 공급자의 상세 정보**를 가져옵니다.
//- 반환 타입은 `ClientRegistration.ProviderDetails`입니다.
//- 이 객체는 인증/토큰/사용자 정보와 관련된 엔드포인트를 제공합니다:
//  - `AuthorizationUri`: 인증 요청 URL
//  - `TokenUri`: 액세스 토큰 요청 URL
//  - `UserInfoEndpoint`: 사용자 정보 엔드포인트 관련 데이터

//### **4. `getUserInfoEndpoint()`**
//`userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()`는 **사용자 정보 엔드포인트 정보**를 가져옵니다.
//- 반환 타입은 `ClientRegistration.ProviderDetails.UserInfoEndpoint`입니다.
//- 이 객체는 사용자 정보를 가져오는 데 필요한 정보들을 제공합니다:
//  - `Uri`: 사용자 정보 엔드포인트의 URL
//  - `AuthenticationMethod`: 인증 방식 (Bearer 토큰 등)
//  - `UserNameAttributeName`: 사용자 정보에서 사용자 이름을 구분하는 속성 이름

//### **5. `getUserNameAttributeName()`**
//`userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName()`는 **사용자 정보 엔드포인트 응답에서 사용자 이름을 식별하는 속성 이름**을 가져옵니다.
//- 이 값은 **OAuth2 공급자가 사용자 정보 응답 JSON에 포함하는 특정 속성**의 이름입니다.
//  - 예: Google의 경우 `sub`(subject), GitHub의 경우 `id`일 수 있습니다.
//- 반환값은 String 타입입니다.

//### **코드의 동작 요약**
//1. `userRequest`에서 클라이언트 등록 정보(`ClientRegistration`)를 가져옵니다.
//2. 이 등록 정보에서 OAuth2 공급자의 상세 정보(`ProviderDetails`)를 참조합니다.
//3. 그중 사용자 정보 엔드포인트(`UserInfoEndpoint`)를 조회합니다.
//4. 사용자 이름 속성(`UserNameAttributeName`)을 반환합니다.
//
//---
//
//### **실제 반환값 예시**
//- Google: `"sub"` (subject, 사용자를 식별하는 고유 ID)
//- GitHub: `"id"` (사용자 ID)
//- Facebook: `"id"`
//
//---
//
//### **전체 코드 흐름**
//이 코드의 목적은 **OAuth2 공급자가 반환하는 사용자 정보 JSON에서, 사용자 이름 속성의 이름을 가져오는 것**입니다.
//이는 OAuth2 인증 후 사용자 정보를 적절히 매핑하거나 저장하는 데 사용됩니다.
//
//추가적으로, 이 속성은 `DefaultOAuth2UserService`가 사용자 정보를 처리할 때 사용될 수 있습니다.