package com.trifler.trifler_chat_application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class LoginRequestDto {

    @NotBlank(message = "Email should not be blank")
    @Email(message = "Enter a valid email")
    private String email;

    @NotBlank(message = "Password should not be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
