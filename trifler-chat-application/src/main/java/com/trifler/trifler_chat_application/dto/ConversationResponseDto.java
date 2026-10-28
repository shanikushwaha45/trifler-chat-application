package com.trifler.trifler_chat_application.dto;


import com.trifler.trifler_chat_application.entity.ConversationType;

import java.time.LocalDateTime;

public record ConversationResponseDto(
        Long id,
        ConversationType type,
        LocalDateTime createdAt
) {}