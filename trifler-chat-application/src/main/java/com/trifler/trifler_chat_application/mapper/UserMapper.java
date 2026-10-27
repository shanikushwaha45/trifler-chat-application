package com.trifler.trifler_chat_application.mapper;

import com.trifler.trifler_chat_application.dto.UserRequestDto;
import com.trifler.trifler_chat_application.dto.UserResponseDto;
import com.trifler.trifler_chat_application.entity.User;

public class UserMapper {

    public static User toUserEntity(UserRequestDto dto){
        User user=new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }

    public static UserResponseDto toResponseDto(User user){
        UserResponseDto dto=new UserResponseDto();
        dto.setEmail(user.getEmail());
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        return dto;
    }
}
