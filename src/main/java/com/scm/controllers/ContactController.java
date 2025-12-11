package com.scm.controllers;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.helper.AppConstants;
import com.scm.helper.Helper;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/user/contacts")
public class ContactController {
   
private Logger logger = org.slf4j.LoggerFactory.getLogger(ContactController.class);
@Autowired
private   ImageService imageService;

  @Autowired
  private ContactService contactService;

  @Autowired
  private UserService userService;

  // add contact page handler method
  @RequestMapping("/add")
  public String addContactView(Model model) {
    ContactForm contactForm = new ContactForm();
    contactForm.setName("sagar chouhan");
    contactForm.setFavorite(true);
    model.addAttribute("contactForm", contactForm);
    return "user/add_contact";
  }

  @RequestMapping(value = "/add", method = RequestMethod.POST)
  public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result,
      Authentication authentication, HttpSession session) {
    // process the contact form
    if (result.hasErrors()) {
      session.setAttribute("message",
          Message.builder().content("please correct your error ?").type(MessageType.red).build());
      System.out.println("Contact form has errors");
      return "user/add_contact";
    }
    String username = Helper.getEmailOfLoggedInUser(authentication);
    // form --->contact entity
    User user = userService.getUserByEmail(username);
    // process the contact picture
//image process
// logger.info("file information:{}",contactForm.getContactImage().getOriginalFilename());
String  filename = UUID.randomUUID().toString();
 String fileURL = imageService.uploadImage(contactForm.getContactImage(), filename);


    Contact contact = new Contact();
    contact.setName(contactForm.getName());
    contact.setEmail(contactForm.getEmail());
    contact.setPhoneNumber(contactForm.getPhoneNumber());
    contact.setAddress(contactForm.getAddress());
    contact.setDescription(contactForm.getDescription());
    contact.setFavrite(contactForm.isFavorite());
    contact.setWedsiteLink(contactForm.getWebsiteLink());
    contact.setLinkedInLink(contactForm.getLinkedInLink());
    contact.setUser(user);
    contact.setPicture(fileURL);
    contact.setCloudinaryImagePublicId(filename);
    contactService.saveContact(contact);
    System.out.println(contactForm);
    // set the contact picture url
    // set message to be displayed on the view
    session.setAttribute("message",
        Message.builder().content("Your contact has been added successfully!").type(MessageType.green).build());
    return "redirect:/user/contacts/add";
  }

@RequestMapping
  public  String   viewContact(
    @ModelAttribute ContactSearchForm contactSearchForm,
    @RequestParam(value = "page",defaultValue = "0")int page,
    @RequestParam(value = "size",defaultValue = AppConstants.PAGE_SIZE + "")int size,
    @RequestParam(value = "sortField",defaultValue = "name")String sortField,
    @RequestParam(value = "sortDir",defaultValue = "asc")String sortDirection, 
    Model model,Authentication authentication){
    String username=Helper.getEmailOfLoggedInUser(authentication);
  User user =userService.getUserByEmail(username);
  Page<Contact> pageContacts =  contactService.getByUser(user,page,size,sortField,sortDirection);
    model.addAttribute("pageContacts",pageContacts);
    model.addAttribute("pageSize",size);
     model.addAttribute("contactSearchForm",new ContactSearchForm());
    return "user/contacts";
  }

 @RequestMapping(value = "/search", method = RequestMethod.GET)
public String SearchHandler(
 @ModelAttribute ContactSearchForm contactSearchForm,
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
    @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
    @RequestParam(value = "direction", defaultValue = "asc") String direction,
    Model model,
    Authentication authentication
) {

  String field = contactSearchForm.getField();
String value = contactSearchForm.getValue();

if (field == null || field.isBlank()) {
    field = "name"; // default fallback field
}

    logger.info("field={} keyword={}",field,value);

    String username = Helper.getEmailOfLoggedInUser(authentication);
    User user = userService.getUserByEmail(username);

    Page<Contact> pageContacts = Page.empty();

    switch (field.toLowerCase()) {
        case "name":
            pageContacts = contactService.searchByName(user.getUserId(), value, size, page, sortBy, direction);
            break;
        case "email":
            pageContacts = contactService.searchByEmail(user.getUserId(), value, size, page, sortBy, direction);
            break;
        case "phonenumber":
            pageContacts = contactService.searchByPhoneNumber(user.getUserId(), value, size, page, sortBy, direction);
            break;
        default:
            logger.warn("Unknown search field: {}",field);
    }

    model.addAttribute("pageContacts", pageContacts);
    model.addAttribute("pageSize", size);
   
 model.addAttribute("contactSearchForm", contactSearchForm);
  model.addAttribute("keyword",value);
    model.addAttribute("field",field);
    return "user/search";
}


@RequestMapping("/delete/{contactId}")
public String deleteContact(@PathVariable("contactId") String contactId,HttpSession session){
  contactService.deleteContact(contactId);
  logger.info("contactId {} deleted",contactId);

  session.setAttribute("message",Message.builder().content("contact is deleted successfully !!")
  .type(MessageType.green).build());
  return "redirect:/user/contacts";

}

//update contact form 
@GetMapping("/view/{contactId}")
public String updateContactFormView(@PathVariable String contactId,Model model){
  var contact= contactService.getById(contactId);
  ContactForm contactForm =new ContactForm();
  contactForm.setName(contact.getName());
  contactForm.setEmail(contact.getEmail());
   contactForm.setPhoneNumber(contact.getPhoneNumber());
    contactForm.setAddress(contact.getAddress());
     contactForm.setDescription(contact.getDescription());
      contactForm.setFavorite(contact.isFavrite());
      contactForm.setPicture(contact.getPicture());
       contactForm.setWebsiteLink(contact.getWedsiteLink());
        contactForm.setLinkedInLink(contact.getLinkedInLink());


  model.addAttribute("contactForm",contactForm);
  model.addAttribute("contactId",contactId);
return "user/update_contact_view";

}

@RequestMapping(value = "/update/{contactId}", method = RequestMethod.POST)
public String updateContact(
        @PathVariable("contactId") String contactId,
        @Valid @ModelAttribute ContactForm contactForm,
        BindingResult bindingResult,
        Model model,
        HttpSession session) {

    if (bindingResult.hasErrors()) {
        return "user/update_contact_view";
    }

    // 🔹 Fetch existing contact from DB
    var existingContact = contactService.getById(contactId);

    existingContact.setName(contactForm.getName());
    existingContact.setEmail(contactForm.getEmail());
    existingContact.setPhoneNumber(contactForm.getPhoneNumber());
    existingContact.setAddress(contactForm.getAddress());
    existingContact.setDescription(contactForm.getDescription());
    existingContact.setFavrite(contactForm.isFavorite());
    existingContact.setWedsiteLink(contactForm.getWebsiteLink());
    existingContact.setLinkedInLink(contactForm.getLinkedInLink());

    // 🔹 Process image upload only if new file is uploaded
    if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
        logger.info("New image uploaded...");

        // Generate a new file name and upload to Cloudinary
        String fileName = UUID.randomUUID().toString();
        String imageUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);

        // ✅ Update only the new image info
        existingContact.setCloudinaryImagePublicId(fileName);
        existingContact.setPicture(imageUrl);

        // Reflect it in the form for confirmation/view
        contactForm.setPicture(imageUrl);
    } else {
        logger.info("No new image uploaded, keeping existing image.");
    }

    // 🔹 Update contact in DB
    var updatedContact = contactService.updateContact(existingContact);
    logger.info("Updated contact {}", updatedContact);

    session.setAttribute("message",
        Message.builder().content("Contact updated successfully!").type(MessageType.green).build());

    return "redirect:/user/contacts/view/" + contactId;
}

}
