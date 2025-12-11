package com.scm.helper;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

import ch.qos.logback.classic.spi.STEUtil;

public class Helper {
  
    public static String  getEmailOfLoggedInUser(Authentication authentication){

        //ager email and password login ----->email found
        if(authentication instanceof OAuth2AuthenticationToken){

           var aOAuth2AuthenticationToken  = (OAuth2AuthenticationToken)authentication;
           var clientId = aOAuth2AuthenticationToken.getAuthorizedClientRegistrationId(); 
    
           var oauth2User=(OAuth2User)authentication.getPrincipal();
           String username="";

           if(clientId.equalsIgnoreCase("google")){

             //login with google
           System.out.println("Getting email from google");
           username = oauth2User.getAttribute("email").toString();
           }else if (clientId.equalsIgnoreCase("github")) {
              //login with github  

               System.out.println("Getting email from github");
                username = oauth2User.getAttribute("email") != null
                    ? oauth2User.getAttribute("email").toString()
                    : oauth2User.getAttribute("login").toString() + "@gmail.com";

           }
       
   //login with linkedin
      

        return username;
        }else{
             System.out.println("Getting email from local DB");
           return  authentication.getName();
        }
 
    }


  public static String getEmailForEmailVerification(String emailToken){
     String link = "http://localhost:8081/auth/verify-email?token="+emailToken;

     return link;

  }






  }


