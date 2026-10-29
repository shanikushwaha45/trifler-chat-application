
package com.trifler.trifler_chat_application.service;

import com.trifler.trifler_chat_application.dto.CreateGroupRequestDto;
import com.trifler.trifler_chat_application.dto.GroupDetailsResponseDto;
import com.trifler.trifler_chat_application.dto.GroupMemberResponseDto;
import com.trifler.trifler_chat_application.entity.Conversation;
import com.trifler.trifler_chat_application.entity.ConversationParticipant;
import com.trifler.trifler_chat_application.entity.ConversationType;
import com.trifler.trifler_chat_application.entity.User;
import com.trifler.trifler_chat_application.repository.ConversationParticipantRepository;
import com.trifler.trifler_chat_application.repository.ConversationRepository;
import com.trifler.trifler_chat_application.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository participantRepository;
    private final UserRepository userRepository;

    public ConversationService(
            ConversationRepository conversationRepository,
            ConversationParticipantRepository participantRepository,
            UserRepository userRepository) {
        this.conversationRepository = conversationRepository;
        this.participantRepository = participantRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Long createGroup(CreateGroupRequestDto request) {

        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Group name is required");
        }

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String loggedInEmail = authentication.getName();

        User creator = userRepository.findByEmail(loggedInEmail)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Logged-in user not found"));

        Conversation conversation = new Conversation();
        conversation.setName(request.getName().trim());
        conversation.setType(ConversationType.GROUP);

// Set group creator
        conversation.setCreatedBy(creator);

// Save group
        Conversation savedConversation =
                conversationRepository.save(conversation);

        addParticipant(savedConversation, creator);

        List<String> memberEmails = request.getMemberEmails();

        if (memberEmails != null) {
            for (String email : memberEmails) {

                if (email == null || email.isBlank()) {
                    continue;
                }

                User member = userRepository.findByEmail(email.trim())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "User not found: " + email));

                if (!member.getId().equals(creator.getId())) {
                    addParticipant(savedConversation, member);
                }
            }
        }

        return savedConversation.getId();
    }

    @Transactional
    public void addGroupMember(Long conversationId, Long userId) {

        // Find group
        Conversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Conversation not found"));

        // Only GROUP conversations are allowed
        if (conversation.getType() != ConversationType.GROUP) {
            throw new IllegalArgumentException(
                    "This conversation is not a group");
        }

        // Get logged-in user
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String loggedInEmail = authentication.getName();

        User loggedInUser = userRepository.findByEmail(loggedInEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("Logged-in user not found"));

        // Only group creator can add members
        if (conversation.getCreatedBy() == null ||
                !conversation.getCreatedBy().getId().equals(loggedInUser.getId())) {
            throw new IllegalArgumentException(
                    "Only the group creator can add members");
        }

        // Find user to add
        User member = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        // Add participant if not already a member
        addParticipant(conversation, member);
    }

    private void addParticipant(Conversation conversation, User user) {

        if (!participantRepository.existsByConversationIdAndUserId(
                conversation.getId(), user.getId())) {

            ConversationParticipant participant =
                    new ConversationParticipant();

            participant.setConversation(conversation);
            participant.setUser(user);

            participantRepository.save(participant);
        }
    }

    @Transactional(readOnly = true)
    public GroupDetailsResponseDto getGroupDetails(Long conversationId) {

        Conversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Group not found"));

        if (conversation.getType() != ConversationType.GROUP) {
            throw new IllegalArgumentException(
                    "This conversation is not a group");
        }

        List<ConversationParticipant> participants =
                participantRepository.findByConversationId(conversationId);

        List<GroupMemberResponseDto> members = participants.stream()
                .map(participant -> {
                    GroupMemberResponseDto dto =
                            new GroupMemberResponseDto();

                    dto.setId(participant.getUser().getId());
                    dto.setName(participant.getUser().getName());
                    dto.setEmail(participant.getUser().getEmail());

                    return dto;
                })
                .toList();

        GroupDetailsResponseDto response = new GroupDetailsResponseDto();
        response.setGroupId(conversation.getId());
        response.setGroupName(conversation.getName());
        response.setMembers(members);

        return response;
    }

    @Transactional
    public void removeGroupMember(Long conversationId, Long userId) {

        Conversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Group not found"));

        if (conversation.getType() != ConversationType.GROUP) {
            throw new IllegalArgumentException(
                    "This conversation is not a group");
        }

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String loggedInEmail = authentication.getName();

        User loggedInUser = userRepository.findByEmail(loggedInEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("Logged-in user not found"));

        if (conversation.getCreatedBy() == null ||
                !conversation.getCreatedBy().getId()
                        .equals(loggedInUser.getId())) {

            throw new IllegalArgumentException(
                    "Only the group creator can remove members");
        }

        if (conversation.getCreatedBy().getId().equals(userId)) {
            throw new IllegalArgumentException(
                    "Group creator cannot remove themselves");
        }

        List<ConversationParticipant> participants =
                participantRepository.findByConversationId(conversationId);

        ConversationParticipant participant = participants.stream()
                .filter(p -> p.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User is not a member of this group"));

        participantRepository.delete(participant);
    }
}
