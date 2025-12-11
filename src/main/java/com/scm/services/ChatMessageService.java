// package com.scm.services;

// import java.util.ArrayList;
// import java.util.Comparator;
// import java.util.List;
// import java.util.UUID;

// import org.springframework.stereotype.Service;

// import com.scm.entities.ChatMessage;
// import com.scm.repositories.ChatMessageRepository;

// import lombok.RequiredArgsConstructor;

// @Service
// @RequiredArgsConstructor
// public class ChatMessageService {

//     private final ChatMessageRepository repo;
//     private final EmailService emailService;

//     // ---------------------------
//     // GET CHAT BETWEEN TWO USERS
//     // ---------------------------
//     public List<ChatMessage> getChat(String userId, String contactId) {

//         // Messages sent by "userId" to "contactId"
//         List<ChatMessage> sent = repo
//                 .findBySenderIdAndReceiverIdOrderByTimestampAsc(userId, contactId);

//         // Messages received by "userId" from "contactId"
//         List<ChatMessage> received = repo
//                 .findByReceiverIdAndSenderIdOrderByTimestampAsc(userId, contactId);

//         // Combine both lists
//         List<ChatMessage> total = new ArrayList<>();
//         total.addAll(sent);
//         total.addAll(received);

//         // Sort by timestamp
//         total.sort(Comparator.comparingLong(ChatMessage::getTimestamp));

//         return total;
//     }

//     // ---------------------------
//     // SAVE MESSAGE + SEND EMAIL
//     // ---------------------------
//     public ChatMessage save(ChatMessage msg) {

//         // Generate ID + Timestamp
//         msg.setId(UUID.randomUUID().toString());
//         msg.setTimestamp(System.currentTimeMillis());

//         // Save chat in DB
//         ChatMessage saved = repo.save(msg);

//         // EMAIL NOTIFICATION
//         try {
//           emailService.sendChatNotificationEmail(
//         receiverEmail,
//         msg.getSenderName(),
//         msg.getContent()
// );
//         } catch (Exception e) {
//             System.out.println("Email sending failed: " + e.getMessage());
//         }

//         return saved;
//     }
// }


package com.scm.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.scm.entities.ChatMessage;
import com.scm.repositories.ChatMessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository repo;
    private final EmailService emailService;

    // ----------------------------------------------------
    // FETCH CHAT BETWEEN TWO USERS
    // ----------------------------------------------------
   public List<ChatMessage> getChatByEmails(String senderEmail, String receiverEmail) {
    List<ChatMessage> sent = repo.findBySenderEmailAndReceiverEmailOrderByTimestampAsc(senderEmail, receiverEmail);
    List<ChatMessage> received = repo.findBySenderEmailAndReceiverEmailOrderByTimestampAsc(receiverEmail, senderEmail);

    List<ChatMessage> total = new ArrayList<>();
    total.addAll(sent);
    total.addAll(received);

    total.sort(Comparator.comparingLong(ChatMessage::getTimestamp));

    return total;
}

    // ----------------------------------------------------
    // SAVE MESSAGE + SEND EMAIL NOTIFICATION
    // ----------------------------------------------------
    public ChatMessage save(ChatMessage msg) {

        msg.setId(UUID.randomUUID().toString());
        msg.setTimestamp(System.currentTimeMillis());

        ChatMessage saved = repo.save(msg);

        // -------------------------------
        // EMAIL NOTIFICATION
        // -------------------------------
        try {
            emailService.sendChatNotificationEmail(
                msg.getReceiverEmail(),       // Email of receiver
                msg.getSenderName(),          // Sender Name
              msg.getText()             // Message content
            );
        } catch (Exception ex) {
            System.out.println("Email sending failed: " + ex.getMessage());
        }

        return saved;
    }
}
