package com.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.entities.Providers;
import com.scm.entities.User;
import com.scm.helper.AppConstants;
import com.scm.repositories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenicationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuthAuthenicationSuccessHandler.class);

    private final UserRepo userRepo;

    // ✅ Constructor injection ensures Spring injects the repository
    @Autowired
    public OAuthAuthenicationSuccessHandler(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        logger.info("OAuthenticationSuccessHandler");

        var OAuthAuthenicationToken = (OAuth2AuthenticationToken) authentication;
        String authorizedClientRegistrationId = OAuthAuthenicationToken.getAuthorizedClientRegistrationId();

        logger.info("Provider: {}", authorizedClientRegistrationId);

        var oauthUser = (DefaultOAuth2User) authentication.getPrincipal();
        oauthUser.getAttributes().forEach((key, value) -> logger.info("{} : {}", key, value));

        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        user.setEmailVerified(true);
        user.setEnabled(true);
        user.setPassword("dummy");

        if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {
            user.setEmail(oauthUser.getAttribute("email").toString());
            user.setProfilePic(oauthUser.getAttribute("picture").toString());
         user.setName(oauthUser.getAttribute("name").toString());
          user.setProviderUserId(oauthUser.getAttribute("sub").toString());
               user.setProvider(Providers.GOOGLE);
               user.setAbout("this account is created using  Google.");

        } else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {
            String email = oauthUser.getAttribute("email") != null
                    ? oauthUser.getAttribute("email").toString()
                    : oauthUser.getAttribute("login").toString() + "@gmail.com";
            String picture = oauthUser.getAttribute("avatar_url").toString();
            String name = oauthUser.getAttribute("login").toString();
            String providerUserId = oauthUser.getName();

            user.setEmail(email);
            user.setProfilePic(picture);
            user.setName(name);
            user.setProviderUserId(providerUserId);
            user.setProvider(Providers.GITHUB);
             user.setAbout("this account is created using  GitHub.");
        } else if (authorizedClientRegistrationId.equalsIgnoreCase("linkedin")) {
            // TODO: handle LinkedIn
        } else {
            logger.info("Unknown provider");
        }

        // ✅ userRepo is now injected and safe to use
        User existingUser = userRepo.findByEmail(user.getEmail()).orElse(null);
        if (existingUser == null) {
            userRepo.save(user);
          System.out.println("user saved "+user.getEmail());
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
    }
}
