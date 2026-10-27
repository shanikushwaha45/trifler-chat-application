package com.trifler.trifler_chat_application.controller;

import com.trifler.trifler_chat_application.dto.UserRequestDto;
import com.trifler.trifler_chat_application.dto.UserResponseDto;
import com.trifler.trifler_chat_application.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody @Valid UserRequestDto requestDto){
        return userService.registerUser(requestDto);
    }
}
