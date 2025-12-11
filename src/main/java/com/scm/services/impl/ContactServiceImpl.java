package com.scm.services.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helper.ResourceNotFoundException;
import com.scm.repositories.ContactRepo;
import com.scm.services.ContactService;
@Service
public class ContactServiceImpl implements ContactService{

    @Autowired
    private ContactRepo contactRepo;

    @Override
    public Contact saveContact(Contact contact) {
        String id=UUID.randomUUID().toString();
        contact.setId(id);
        return contactRepo.save(contact);
    }

    // @Override
    // public Contact updateContact(Contact contact) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'updateContact'");
    // }

    @Override
    public List<Contact> getAll() {
        return contactRepo.findAll();
    }

    @Override
    public Contact getById(String id) {
        return contactRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Contact not found with given id:"+id));
    }

    // @Override
    // public List<Contact> search(String name, String email, String phoneNumber) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'search'");
    // }

    @Override
    public void deleteContact(String id) {
        var contact = contactRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Contact not found with given id:"+id));
      contactRepo.delete(contact);
    }

    @Override
    public List<Contact> getByUserId(String userId) {
      return  contactRepo.findByUserId(userId);

    }

    @Override
    public Page<Contact> getByUser(User user,int page,int size,String  sortField,String sortDirection) {
        Sort sort = sortDirection.equals("desc")? Sort.by(sortField).descending():Sort.by(sortField).ascending();
        var pageable = PageRequest.of(page, size, sort);
       return contactRepo.findByUser(user,pageable);
    }

    @Override
public Contact updateContact(Contact contact) {
    var contactOld = contactRepo.findById(contact.getId()).orElseThrow(()-> new ResourceNotFoundException("Contact not found"));
    contactOld.setName(contact.getName());
    contactOld.setEmail(contact.getEmail());
    contactOld.setPhoneNumber(contact.getPhoneNumber());
    contactOld.setAddress(contact.getAddress());
    contactOld.setDescription(contact.getDescription());
    contactOld.setPicture(contact.getPicture());
    contactOld.setFavrite(contact.isFavrite());
    contactOld.setWedsiteLink(contact.getWedsiteLink());
    contactOld.setLinkedInLink(contact.getLinkedInLink());
    contactOld.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());

    return contactRepo.save(contactOld);
}

   @Override
public Page<Contact> searchByName(String userId, String nameKeyword, int size, int page, String sortBy, String order) {
    Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    Pageable pageable = PageRequest.of(page, size, sort);
    return contactRepo.searchByName(userId, nameKeyword, pageable);
}

@Override
public Page<Contact> searchByEmail(String userId, String emailKeyword, int size, int page, String sortBy, String order) {
    Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    Pageable pageable = PageRequest.of(page, size, sort);
    return contactRepo.searchByEmail(userId, emailKeyword, pageable);
}

@Override
public Page<Contact> searchByPhoneNumber(String userId, String phoneNumberKeyword, int size, int page, String sortBy, String order) {
    Sort sort = order.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    Pageable pageable = PageRequest.of(page, size, sort);
    return contactRepo.searchByPhoneNumber(userId, phoneNumberKeyword, pageable);
}

@Override
public Contact getByEmailAndUser(String email, String userId) {
    return contactRepo.findByEmailAndUser_UserId(email, userId)
            .orElse(null);
}



}

    
