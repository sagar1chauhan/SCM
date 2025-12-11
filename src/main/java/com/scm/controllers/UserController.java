package com.scm.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.User;
import com.scm.helper.Helper;
import com.scm.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

  private Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private UserService userService;

 
  // user Dashboard Page
  @RequestMapping("/dashboard")
  public String userDashboard() {
    System.out.println("User Dashboard");
    return "user/dashboard";

  }

  // user Profile page
  @RequestMapping("/profile")
  public String userProfile(Model model, Authentication authentication) {
    String username = Helper.getEmailOfLoggedInUser(authentication);
    logger.info("User logged in:{}", username);
    // database se data ko fetch :using email
    User user = userService.getUserByEmail(username);
    // System.out.println(user.getName());
    // System.out.println(user.getEmail());
    model.addAttribute("loggedInUser", user);

    return "user/profile";

  }
}
