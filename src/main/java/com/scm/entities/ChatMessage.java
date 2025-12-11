package com.scm.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "chat_messages")
@Data
public class ChatMessage {
    @Id
    private String id;

  private String senderId;
private String receiverId;

private String senderEmail;
private String receiverEmail;

private String senderName;
private String receiverName;

private String text;
private long timestamp;


}
