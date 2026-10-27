package com.trifler.trifler_chat_application.service;

import com.trifler.trifler_chat_application.config.PasswordConfig;
import com.trifler.trifler_chat_application.dto.UserRequestDto;
import com.trifler.trifler_chat_application.dto.UserResponseDto;
import com.trifler.trifler_chat_application.entity.User;
import com.trifler.trifler_chat_application.exception.DuplicateResourceException;
import com.trifler.trifler_chat_application.mapper.UserMapper;
import com.trifler.trifler_chat_application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<UserResponseDto> registerUser(UserRequestDto userRequestDto){


        log.info("User Registration Started:");
        if(userRepository.existsByEmail(userRequestDto.getEmail())){
            log.info("The email passed is already exists");
            throw new DuplicateResourceException("Email Already Exist ");
        }

        if(userRepository.existsByUsername(userRequestDto.getUsername())){
            log.info("The username passed is already exists");
            throw new DuplicateResourceException("UserName already exist enter unique username");
        }

        log.info("The user Registration is sucsesfull");
        userRequestDto.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        User user =userRepository.save(UserMapper.toUserEntity(userRequestDto));



        return  ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toResponseDto(user));
    }

}
