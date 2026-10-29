
package com.trifler.trifler_chat_application.controller;

import com.trifler.trifler_chat_application.dto.ConversationResponseDto;
import com.trifler.trifler_chat_application.dto.GroupDetailsResponseDto;
import com.trifler.trifler_chat_application.dto.MessageResponseDto;
import com.trifler.trifler_chat_application.entity.Conversation;
import com.trifler.trifler_chat_application.service.PrivateConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.trifler.trifler_chat_application.dto.CreateGroupRequestDto;
import com.trifler.trifler_chat_application.service.ConversationService;
import java.util.List;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationController {



    private final PrivateConversationService privateConversationService;
    private final ConversationService conversationService;

    @PostMapping("/private")
    public Long createPrivateConversation(
            @RequestParam String recipientEmail) {

        return privateConversationService
                .createPrivateConversation(recipientEmail);
    }

    @GetMapping("/my")
    public List<ConversationResponseDto> getMyConversations() {
        return privateConversationService.getMyConversations();
    }


    @PostMapping("/{conversationId}/messages")
    public Long sendMessage(
            @PathVariable Long conversationId,
            @RequestParam String content) {

        return privateConversationService
                .sendMessage(conversationId, content);
    }

    @GetMapping("/{conversationId}/messages")
    public List<MessageResponseDto> getMessages(
            @PathVariable Long conversationId) {

        return privateConversationService.getMessages(conversationId);
    }

    @PostMapping("/group")
    public Long createGroup(@RequestBody CreateGroupRequestDto request) {
        return conversationService.createGroup(request);
    }

    @PostMapping("/group/{conversationId}/members/{userId}")
    public String addGroupMember(
            @PathVariable Long conversationId,
            @PathVariable Long userId) {

        conversationService.addGroupMember(conversationId, userId);

        return "Group member added successfully";
    }

    @GetMapping("/{conversationId}/details")
    public GroupDetailsResponseDto getGroupDetails(
            @PathVariable Long conversationId) {

        return conversationService.getGroupDetails(conversationId);
    }

    @DeleteMapping("/group/{conversationId}/members/{userId}")
    public String removeGroupMember(
            @PathVariable Long conversationId,
            @PathVariable Long userId) {

        conversationService.removeGroupMember(conversationId, userId);

        return "Group member removed successfully";
    }

}
