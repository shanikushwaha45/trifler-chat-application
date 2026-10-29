package com.trifler.trifler_chat_application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupMemberResponseDto {

    private Long id;
    private String name;
    private String email;
}