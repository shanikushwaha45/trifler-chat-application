
package com.trifler.trifler_chat_application.repository;

import com.trifler.trifler_chat_application.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findByConversationIdOrderByCreatedAtDesc(
            Long conversationId,
            Pageable pageable
    );

    long countByConversationIdAndReadFalseAndSenderIdNot(Long conversationId, Long senderId);

    List<Message> findByConversationIdAndReadFalse(Long conversationId);

}
