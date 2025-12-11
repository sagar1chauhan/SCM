package com.scm.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.scm.entities.ChatMessage;
import com.scm.services.ChatMessageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatService;

    /**
     * Get chat history between two users (by email)
     */
    @GetMapping("/{userEmail}/{contactEmail}")
    public List<ChatMessage> getMessages(@PathVariable String userEmail,
                                         @PathVariable String contactEmail) {
        // Fetch all messages between these two users, sorted by timestamp
        return chatService.getChatByEmails(userEmail, contactEmail);
    }

    /**
     * Send a chat message
     */
    @PostMapping("/send")
    public ChatMessage send(@RequestBody ChatMessage msg) {
        return chatService.save(msg);
    }
}




// @RestController
// @RequestMapping("/api/chat")
// public class ChatController {

//        private final List<ChatMessage> messages = new CopyOnWriteArrayList<>();

//     @GetMapping("/{userId}/{contactId}")
//     public List<ChatMessage> getMessages(@PathVariable String userId, @PathVariable String contactId) {
//         return messages.stream()
//                 .filter(m ->
//                         (m.getSenderId().equals(userId) && m.getReceiverId().equals(contactId))
//                      || (m.getSenderId().equals(contactId) && m.getReceiverId().equals(userId))
//                 )
//                 .toList();
//     }

//     @PostMapping("/send")
//     public ChatMessage send(@RequestBody ChatMessage msg) {
//         msg.setId(UUID.randomUUID().toString());
//         messages.add(msg);
//         return msg;
//     }
// }

