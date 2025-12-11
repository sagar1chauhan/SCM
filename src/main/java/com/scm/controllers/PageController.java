package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
public class PageController {
    @Autowired
    private UserService userService;

 @GetMapping("/")
 public String index() {
     return "redirect:/home";
 }
 

    @RequestMapping("/home")
    public String home(Model model) {
        System.out.println("Home Page handler");

        model.addAttribute("name", "Substring");
        model.addAttribute("youtubeChannel", "sagartechnical");
        model.addAttribute("linkedin", "https://www.linkedin.com/in/sagar172/");

        return "home"; // this looks for home.html in src/main/resources/templates/
    }

    @RequestMapping("/about")
    public String aboutPage() {
        System.out.println("about Page loading");
        return "about"; // this looks for about.html in src/main/resources/templates/
    }

     @RequestMapping("/services")
    public String servicesPage() {
        System.out.println("services Page loading");
        return "services"; // this looks for services.html in src/main/resources/templates/
    }
     @GetMapping("/contact")
    public String contact() {
        System.out.println("contact Page loading");
        return "contact"; 
    }
     @GetMapping("/login")
    public String login() {
        System.out.println("login Page loading");
        return "login"; 
    }
     @GetMapping("/register")
    public String register(Model model) {


UserForm userForm = new  UserForm();
//default data bhi dal sakte hai
// userForm.setName("sagar chouhan");
// userForm.setAbout("sagar chouhan is a good Java developer...");
model.addAttribute("userForm", userForm);
        return "register"; 
    }
//processing register
    @RequestMapping(value = "/do-register", method=RequestMethod.POST)
    
    public String processRegister(@Valid @ModelAttribute UserForm userForm,BindingResult rBindingResult,HttpSession session) {
        System.out.println("Processing registration");
        //fetching form data
        //Userform
        System.out.println(userForm);
      
        //validate form data 
  if(rBindingResult.hasErrors()){
    return "register";   
  }
        // save to database
        // User  user =   User.builder()
        // .name(userForm.getName())
        // .email(userForm.getEmail())
        // .password(userForm.getPassword())
        // .about(userForm.getAbout())
        // .phoneNumber(userForm.getPhoneNumber())
        // .profilePic("src\\main\\resources\\static\\images\\Default.jpg")
        // .build();
        User user = new User();
        user.setName(userForm.getName());
       user.setEmail(userForm.getEmail()); 
       user.setPassword(userForm.getPassword());
       user.setAbout(userForm.getAbout());
       user.setPhoneNumber(userForm.getPhoneNumber());
       user.setEnabled(false);
       user.setProfilePic("src\\main\\resources\\static\\images\\Default.jpg");



      User saveUser=  userService.saveUser(user);
      System.out.println("Saved user:"+saveUser);
        //message ="Registration Successful"
        //add the messge
    Message message = Message.builder().content("Registration successful").type(MessageType.blue).build();
        session.setAttribute("message", message);
        //redirect to login page
        return "redirect:/register";
    }
    
}
