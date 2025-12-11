package com.scm.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.services.ContactService;
import com.scm.services.UserService;

@Controller
@RequestMapping("/user")
public class DashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model) {

        // logged in user email (username)
        String email = principal.getName();

        // get user
        User user = userService.getUserByEmail(email);

        // total contacts
        List<Contact> contacts = contactService.getByUserId(user.getUserId());
        int totalContacts = contacts.size();

        // favorite contacts
        long favCount = contacts.stream()
                .filter(Contact::isFavrite)
                .count();

        // last 5 contacts
        List<Contact> lastFive = contacts.stream()
                .sorted((a,b) -> b.getId().compareTo(a.getId()))
                .limit(5)
                .toList();

        // add to model
        model.addAttribute("user", user);
        model.addAttribute("totalContacts", totalContacts);
        model.addAttribute("favCount", favCount);
        model.addAttribute("recentContacts", lastFive);

        return "user/dashboard";
    }
}
