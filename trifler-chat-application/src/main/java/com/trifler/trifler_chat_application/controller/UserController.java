package com.trifler.trifler_chat_application.controller;

import com.trifler.trifler_chat_application.dto.LoginRequestDto;
import com.trifler.trifler_chat_application.dto.LoginResponseDto;
import com.trifler.trifler_chat_application.dto.UserRequestDto;
import com.trifler.trifler_chat_application.dto.UserResponseDto;
import com.trifler.trifler_chat_application.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody @Valid UserRequestDto requestDto){
        return userService.registerUser(requestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto){
        return userService.login(loginRequestDto);
    }
    @GetMapping("/protected")
    public Map<String, String> protectedEndpoint() {
        return Map.of(
                "message",
                "JWT authentication successful!"
        );
    }


}
