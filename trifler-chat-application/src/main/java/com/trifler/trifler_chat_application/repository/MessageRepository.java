package com.trifler.trifler_chat_application.repository;

import com.trifler.trifler_chat_application.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {

    List<Message> findByConversationIdOrderByCreatedAtAsc(Long conversationId);
}
