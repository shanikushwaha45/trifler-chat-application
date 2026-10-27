package com.trifler.trifler_chat_application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {

    private String message;
    private String token;
    private Long id;
    private String email;
}