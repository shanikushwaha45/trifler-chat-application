
package com.trifler.trifler_chat_application.service;

import com.trifler.trifler_chat_application.dto.ConversationResponseDto;
import com.trifler.trifler_chat_application.dto.MessageResponseDto;
import com.trifler.trifler_chat_application.entity.*;
import com.trifler.trifler_chat_application.exception.InvalidCredentialException;
import com.trifler.trifler_chat_application.exception.ResourceNotFoundException;
import com.trifler.trifler_chat_application.repository.ConversationParticipantRepository;
import com.trifler.trifler_chat_application.repository.ConversationRepository;
import com.trifler.trifler_chat_application.repository.MessageRepository;
import com.trifler.trifler_chat_application.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivateConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;


    @Transactional
    public Long createPrivateConversation(String recipientEmail) {

        // 1. Get the loggedin user's email
        String senderEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        // Find the sender in the database
        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));

        // Find the recipient in the database
        User recipient = userRepository.findByEmail(recipientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));

        // Get all existing private conversations
        List<Conversation> conversations =
                conversationRepository.findByType(ConversationType.PRIVATE);

        // Check whether both users already have a private chat
        for (Conversation chat : conversations) {

            List<ConversationParticipant> participants =
                    participantRepository
                            .findByConversationId(chat.getId());

            boolean senderExists = participants.stream()
                    .anyMatch(p -> p.getUser().getId().equals(sender.getId()));

            boolean recipientExists = participants.stream()
                    .anyMatch(p -> p.getUser().getId().equals(recipient.getId()));

            // If both users are in this two-person chat, reuse it
            if (senderExists && recipientExists && participants.size() == 2) {
                return chat.getId();
            }
        }

        //No existing chat found, so create a new conversation
        Conversation conversation = new Conversation();
        conversation.setType(ConversationType.PRIVATE);

        // Add the sender as a participant
        ConversationParticipant p1 = new ConversationParticipant();
        p1.setConversation(conversation);
        p1.setUser(sender);
        conversation.getParticipants().add(p1);

        // Add the recipient as a participant
        ConversationParticipant p2 = new ConversationParticipant();
        p2.setConversation(conversation);
        p2.setUser(recipient);
        conversation.getParticipants().add(p2);

        //Save the conversation and return its ID
        return conversationRepository.save(conversation).getId();
    }

    @Transactional(readOnly = true)
    public List<ConversationResponseDto> getMyConversations() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<ConversationParticipant> participants =
                participantRepository.findByUserId(user.getId());

        List<ConversationResponseDto> conversations =
                new ArrayList<>();

        for (ConversationParticipant participant : participants) {

            Conversation conversation = participant.getConversation();

            ConversationResponseDto dto = new ConversationResponseDto(
                    conversation.getId(),
                    conversation.getType(),
                    conversation.getCreatedAt()
            );

            conversations.add(dto);
        }

        return conversations;
    }


    @Transactional
    public Long sendMessage(Long conversationId, String content) {


        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();


        User sender = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        Conversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));

        // Check whether the sender belongs to this conversation
        boolean isParticipant = participantRepository
                .existsByConversationIdAndUserId(
                        conversationId, sender.getId());

        if (!isParticipant) {
            throw new ResourceNotFoundException("You are not a participant in this conversation");
        }


        Message message = new Message();
        message.setContent(content);
        message.setSender(sender);
        message.setConversation(conversation);


        return messageRepository.save(message).getId();
    }

    @Transactional(readOnly = true)
    public List<MessageResponseDto> getMessages(Long conversationId) {


        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();


        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));


        conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));


        boolean isParticipant =
                participantRepository
                        .existsByConversationIdAndUserId(
                                conversationId, user.getId());

        if (!isParticipant) {
            throw new RuntimeException(
                    "You are not a participant in this conversation");
        }


        List<Message> messages =
                messageRepository
                        .findByConversationIdOrderByCreatedAtAsc(conversationId);


        List<MessageResponseDto> responseList = new ArrayList<>();

        for (Message message : messages) {
            MessageResponseDto responseDto = new MessageResponseDto(
                    message.getId(),
                    message.getContent(),
                    message.getSender().getId(),
                    message.getSender().getName(),
                    message.getCreatedAt()
            );

            responseList.add(responseDto);
        }

        return responseList;
    }

}
