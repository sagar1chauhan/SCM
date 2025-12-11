package com.scm.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.scm.services.impl.SecurityCustomUserDetailService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
 



@Configuration
public class SecurityConfig {
   //user creste login using  java code with in memory service
  
//    @Bean
//    public  UserDetailsService userDetailsService(){
//     UserDetails user1 =User
//     .withDefaultPasswordEncoder()
//     .username("admin123")
//     .password("admin123")
//     .roles("ADMIN","USER")
//     .build();
//      UserDetails user2 =User
//     .withDefaultPasswordEncoder()
//     .username("user123")
//     .password("user123")
//     // .roles("ADMIN","USER")
//     .build();
//     var InMemoryUserDetailsManager=new InMemoryUserDetailsManager(user1,user2);
//     return InMemoryUserDetailsManager;
       
// }
@Autowired
private SecurityCustomUserDetailService userDetailsService;
//configure authentication provider
@Autowired
 private OAuthAuthenicationSuccessHandler handler;

 @Autowired
 private AuthFailureHandler authFailureHandler;
@Bean
public DaoAuthenticationProvider authenticationProvider(){
    DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
    //user Detail Service object
   daoAuthenticationProvider.setUserDetailsService(userDetailsService);
   //password encoder object
    daoAuthenticationProvider.setPasswordEncoder(passswordEncoder());
    return daoAuthenticationProvider;
}

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity  httpSecurity) throws Exception{
    //configuration

    //url which are public and private
    httpSecurity.authorizeHttpRequests
   (authorize->{
        authorize.requestMatchers("/user/**").authenticated();
        authorize.anyRequest().permitAll();
});
//form Default login
//any changes make in form login related so go to this place
httpSecurity.formLogin(formLogin-> {
    formLogin.loginPage("/login");
    formLogin.loginProcessingUrl("/authenticate");
    formLogin.successForwardUrl("/user/profile");
    // formLogin.failureForwardUrl("/login?error=true");
    formLogin.usernameParameter("email");
    formLogin.passwordParameter("password");

      formLogin.failureHandler(authFailureHandler);



 //   formLogin.failureHandler(new AuthenticationFailureHandler(){

//         @Override
//         public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
//                 AuthenticationException exception) throws IOException, ServletException {
//             // TODO Auto-generated method stub
//             throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationFailure'");
//         }
       
    
// });

//      formLogin.successHandler(new AuthenticationSuccessHandler() {

//         @Override
//         public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                 Authentication authentication) throws IOException, ServletException {
//             // TODO Auto-generated method stub
//             throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationSuccess'");
//         }
    
// });

});
    httpSecurity.csrf(AbstractHttpConfigurer::disable);
    //logout
   httpSecurity.logout(logoutForm->{
    logoutForm.logoutUrl("/do-logout");
    logoutForm.logoutSuccessUrl("/login?logout=true");
   });  

httpSecurity.oauth2Login(oauth->{
    oauth.loginPage("/login");
    oauth.successHandler(handler);
    
});

return  httpSecurity.build();
}
@Bean
public PasswordEncoder passswordEncoder(){
    return new BCryptPasswordEncoder();
}
}
