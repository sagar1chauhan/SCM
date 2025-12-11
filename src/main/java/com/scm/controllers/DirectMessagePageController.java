package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.scm.entities.ChatMessage;
import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.services.ChatMessageService;
import com.scm.services.ContactService;
import com.scm.services.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class DirectMessagePageController {

    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    @Autowired
    private ChatMessageService chatMessageService; // ADD THIS

    // OPEN CHAT PAGE (USING CONTACT EMAIL)
    @GetMapping("/direct-message/{email}")
    public String openChatPage(
            @PathVariable("email") String email,
            Principal principal,
            Model model) {

        User loggedUser = userService.getUserByEmail(principal.getName());

        // All contacts
        List<Contact> contacts = contactService.getByUserId(loggedUser.getUserId());

        // Get receiver by email
        Contact receiver = contactService.getByEmailAndUser(email, loggedUser.getUserId());

        if (receiver == null) {
            System.out.println("Receiver is NULL for email = " + email);
            receiver = new Contact(); // prevent Thymeleaf crash
        }

        // 1️⃣ Fetch previous chat messages between loggedUser and receiver
      List<ChatMessage> chatHistory = chatMessageService.getChatByEmails(
        loggedUser.getEmail(), receiver.getEmail()
);

        // Add attributes to model
        model.addAttribute("loggedInUser", loggedUser);
        model.addAttribute("receiver", receiver);
        model.addAttribute("contacts", contacts);
        model.addAttribute("chatHistory", chatHistory); // PASS PREVIOUS MESSAGES

        return "user/direct-message"; // Thymeleaf template
    }
}
