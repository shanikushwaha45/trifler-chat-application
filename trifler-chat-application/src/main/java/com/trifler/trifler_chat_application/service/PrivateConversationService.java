
package com.trifler.trifler_chat_application.service;

import com.trifler.trifler_chat_application.entity.Conversation;
import com.trifler.trifler_chat_application.entity.ConversationParticipant;
import com.trifler.trifler_chat_application.entity.ConversationType;
import com.trifler.trifler_chat_application.entity.User;
import com.trifler.trifler_chat_application.exception.InvalidCredentialException;
import com.trifler.trifler_chat_application.exception.ResourceNotFoundException;
import com.trifler.trifler_chat_application.repository.ConversationParticipantRepository;
import com.trifler.trifler_chat_application.repository.ConversationRepository;
import com.trifler.trifler_chat_application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PrivateConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository participantRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createPrivateConversation(String recipientEmail) {

        // Get logged-in user
        String senderEmail = SecurityContextHolder
                .getContext().getAuthentication().getName();

        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        // Find recipient
        User recipient = userRepository.findByEmail(recipientEmail)
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        // Create conversation
        Conversation conversation = new Conversation();
        conversation.setType(ConversationType.PRIVATE);

        // Add sender
        ConversationParticipant p1 = new ConversationParticipant();
        p1.setConversation(conversation);
        p1.setUser(sender);
        conversation.getParticipants().add(p1);

        // Add recipient
        ConversationParticipant p2 = new ConversationParticipant();
        p2.setConversation(conversation);
        p2.setUser(recipient);
        conversation.getParticipants().add(p2);

        // Save and return conversation ID
        return conversationRepository.save(conversation).getId();
    }
}
