package com.trifler.trifler_chat_application.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateGroupRequestDto {

    private String name;
    private List<String> memberEmails;
}