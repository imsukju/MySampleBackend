```mermaid
sequenceDiagram
    participant U as "User (클라이언트)"
    participant C as "LoginController"
    participant S as "LoginService"
    participant R as "UserRepository"
    participant T as "TokenProvider"

    U->>C: "POST /user/login (email, password)"
    C->>S: "로그인 요청 처리"
    S->>R: "사용자 이메일로 데이터 조회"
    R-->>S: "사용자 데이터 반환"
    S->>S: "비밀번호 확인"
    alt 인증 성공
        S->>T: "JWT Access Token 및 Refresh Token 생성"
        T-->>S: "생성된 토큰 반환"
        S-->>C: "토큰 포함 응답"
        C-->>U: "로그인 성공 및 토큰 반환"
    else 인증 실패
        S-->>C: "인증 실패 메시지"
        C-->>U: "로그인 실패 응답"
    end

    %% 토큰 검증 흐름
    U->>C: "보호된 리소스 요청 (토큰 포함)"
    C->>T: "토큰 유효성 검증"
    alt 유효한 토큰
        T-->>C: "토큰 검증 성공"
        C-->>U: "리소스 접근 허용"
    else 만료된 토큰
        T-->>C: "토큰 검증 실패"
        C-->>U: "재로그인 요청"
    end
