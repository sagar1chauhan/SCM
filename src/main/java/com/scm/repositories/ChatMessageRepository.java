package com.scm.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.scm.entities.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {

    List<ChatMessage> findBySenderEmailAndReceiverEmailOrderByTimestampAsc(String senderEmail, String receiverEmail);
List<ChatMessage> findByReceiverEmailAndSenderEmailOrderByTimestampAsc(String receiverEmail, String senderEmail);

}