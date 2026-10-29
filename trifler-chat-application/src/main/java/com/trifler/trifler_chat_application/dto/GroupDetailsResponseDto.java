package com.trifler.trifler_chat_application.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupDetailsResponseDto {

    private Long groupId;
    private String groupName;
    private List<GroupMemberResponseDto> members;
}