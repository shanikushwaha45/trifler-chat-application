
package com.trifler.trifler_chat_application.dto;

import java.time.LocalDateTime;


public record MessageResponseDto(
        Long id,
        String content,
        Long senderId,
        String senderName,
        LocalDateTime createdAt
) {
}
