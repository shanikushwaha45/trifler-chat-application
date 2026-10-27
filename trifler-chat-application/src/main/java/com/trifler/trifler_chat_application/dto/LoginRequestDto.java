package com.trifler.trifler_chat_application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

    @NotBlank(message = "Username should not be blank")
    String usernameOrEmail;
    @NotBlank(message = "Username should not be blank")
            @Size(min = 8,message = "Password should be ")
    String password;
}
