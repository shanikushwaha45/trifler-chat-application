
package com.trifler.trifler_chat_application.repository;

import com.trifler.trifler_chat_application.entity.Conversation;
import com.trifler.trifler_chat_application.entity.ConversationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    List<Conversation> findByType(ConversationType type);
}
