package com.scm.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.scm.entities.Contact;
import com.scm.entities.User;

public interface ContactService {
//save contact
 Contact saveContact(Contact contact);
//update contact
 Contact updateContact(Contact contact);
 //get contact
 List<Contact> getAll();
 //get contact by id
    Contact getById(String id);
//search contact
Page<Contact> searchByName(String userId, String nameKeyword, int size, int page, String sortBy, String order);
Page<Contact> searchByEmail(String userId, String emailKeyword, int size, int page, String sortBy, String order);
Page<Contact> searchByPhoneNumber(String userId, String phoneNumberKeyword, int size, int page, String sortBy, String order);

//delete contact
    void deleteContact(String id);
    //get  contact by user id
    List<Contact> getByUserId(String userId);
    
    Page<Contact> getByUser(User user,int page,int size,String  sortField,String sortDirection);

       Contact getByEmailAndUser(String email, String userId);

} 