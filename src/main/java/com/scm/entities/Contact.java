package com.scm.entities;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Contact {
    @Id
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String picture;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;
    private boolean favrite = false;
    private String wedsiteLink;
    private String linkedInLink;
    private String CloudinaryImagePublicId;


    @ManyToOne
    @JsonIgnore
    private User user;

    
    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<SocialLink> links = new ArrayList<>();
}