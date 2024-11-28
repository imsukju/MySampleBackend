package com.practiceBackend.practiceBackend.modules.register.DTO;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDtoRequest {
    private String id;
    private String email;
    private String password;
    private String checkPassword;

    public static LoginDtoRequestBuilder builder()
    {
        return new LoginDtoRequestBuilder();
    }


    public static class LoginDtoRequestBuilder {
        private String id;
        private String email;
        private String password;
        private String checkPassword;

        public LoginDtoRequestBuilder id(String id) {
            this.id = id;
            return this;
        }
        public LoginDtoRequestBuilder email(String email) {
            this.email = email;
            return this;
        }
        public LoginDtoRequestBuilder password(String password) {
            this.password = password;
            return this;

        }
        public LoginDtoRequestBuilder checkPassword(String checkPassword) {
            this.checkPassword = checkPassword;
            return this;
        }

        public RegisterDtoRequest build() {
            RegisterDtoRequest loginDtoRequest = new RegisterDtoRequest();
            loginDtoRequest.setId(this.id);
            loginDtoRequest.setEmail(this.email);
            loginDtoRequest.setPassword(this.password);
            loginDtoRequest.setCheckPassword(this.checkPassword);
            return loginDtoRequest;
        }

    }
}
