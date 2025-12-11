package com.scm.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scm.entities.Contact;
import com.scm.entities.User;


@Repository
public interface ContactRepo extends JpaRepository<Contact,String >{

@Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
List<Contact> findByUserId(String userId);
Page<Contact> findByUser(User user, Pageable pageable);

    //  Search by name scoped to a specific user
    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId AND LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Contact> searchByName(@Param("userId") String userId, @Param("keyword") String keyword, Pageable pageable);

    //  Search by email scoped to a specific user
    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId AND LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Contact> searchByEmail(@Param("userId") String userId, @Param("keyword") String keyword, Pageable pageable);

    //  Search by phone number scoped to a specific user
    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId AND LOWER(c.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Contact> searchByPhoneNumber(@Param("userId") String userId, @Param("keyword") String keyword, Pageable pageable);

  Optional<Contact> findByEmailAndUser_UserId(String email, String userId);

    List<Contact> findByUser_UserId(String userId);

}
