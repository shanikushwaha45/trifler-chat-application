
package com.trifler.trifler_chat_application.controller;

import com.trifler.trifler_chat_application.service.PrivateConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final PrivateConversationService privateConversationService;

    @PostMapping("/private")
    public Long createPrivateConversation(
            @RequestParam String recipientEmail) {

        return privateConversationService
                .createPrivateConversation(recipientEmail);
    }
}
