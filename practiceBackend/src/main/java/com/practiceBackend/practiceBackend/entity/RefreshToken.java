package com.practiceBackend.practiceBackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RefreshToken {

    @Id
    private String refreshToken;

    private String jti;

    private String username;


    public static RefreshTokenBuilder builder()
    {
        return new RefreshTokenBuilder();
    }

    public static class RefreshTokenBuilder{
        private String refreshToken;
        private String jti;
        private String username;
        public RefreshTokenBuilder refreshToken(String refreshToken){
            this.refreshToken = refreshToken;
            return this;
        }

        public RefreshTokenBuilder jti(String jti){
            this.jti = jti;
            return this;
        }

        public RefreshTokenBuilder username(String username){
            this.username = username;
            return this;
        }



        public RefreshToken build()
        {
            return new RefreshToken(refreshToken,jti,username);
        }

    }
}
