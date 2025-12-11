package com.scm.entities;

import jakarta.persistence.*;   // this includes Id, Entity, Table, Column, etc.
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name="users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class User implements UserDetails {
    @Id
    private String userId;
    @Column(name="user_name",nullable=false)
    private String name;
    @Column(unique=true,nullable=false)
    private String email;
     @Getter(AccessLevel.NONE)
    private String password;
  @Lob
    @Column(columnDefinition = "TEXT")
    private String about;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String profilePic;

    private String phoneNumber;
     @Getter(AccessLevel.NONE)

    //information
    private boolean enabled=false;
     private boolean emailVerified=false;
      private boolean phoneVerified=false;
      @Enumerated(value = EnumType.STRING)
      // SELF,GOOGLE,FACEBOOK,TWITTER,LINKEDIN,GITHUB
      private Providers provider = Providers.SELF;
      private String providerUserId;
      //one to many mapping in user<==>Contact
      @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch =FetchType.LAZY,orphanRemoval = true)
      private List<Contact>contacts = new ArrayList<>();
    
      @ElementCollection(fetch = FetchType.EAGER)
      private List<String>roleList = new ArrayList<>();

      private String emailToken;

      @Override
      public  Collection<? extends GrantedAuthority> getAuthorities(){
        //list of roles[ROLE_ADMIN,ROLE_NORMAL]
        //convert list of string to list of simpleGrantedAuthority collection  [roles{ROLE_ADMIN,ROLE_NORMAL}]
        Collection<SimpleGrantedAuthority> roles=roleList.stream().map(role->new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        return roles;
      }
  
      //username is email
      @Override
      public String getUsername() {
        return this.email;
      }
      @Override
      public String getPassword() {
      return this.password;
      }
      @Override
      public boolean isAccountNonExpired() {
        return true;
      }
      @Override
      public boolean isAccountNonLocked() {
        return true;
      }
      @Override
      public boolean isCredentialsNonExpired() {
        return true;
      }
      @Override
      public boolean isEnabled(){
        return this.enabled;
      }
}
