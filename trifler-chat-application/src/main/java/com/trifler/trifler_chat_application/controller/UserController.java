package com.trifler.trifler_chat_application.controller;

import com.trifler.trifler_chat_application.dto.*;
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
//
//    @GetMapping("/get")
//    public String getString(){
//        return "Hello";
//    }
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody @Valid UserRequestDto requestDto){
        return userService.registerUser(requestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto){
        return userService.login(loginRequestDto);
    }

    @GetMapping("/search/{email}")
    public ResponseEntity<UserSearchResponseDto> searchUserByEmail(@PathVariable String email) {
        return userService.searchUserByEmail(email);
    }


}
