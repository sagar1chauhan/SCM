package com.scm;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.scm.services.EmailService;

@SpringBootTest
class ScmApplicationTests {

	// @Test
	// void contextLoads() {

	// }

	// @Autowired
	// private EmailService service;
	// @Test
	// void sendEmailTest(){
	// 	service.sendEmail("test@gmail.com", "Just testing  email service", "this is scm project working on emial service");
	// }

	@MockBean
    private EmailService emailService;

    @Test
    void sendEmailTest() {
        doNothing()
                .when(emailService)
                .sendEmail(anyString(), anyString(), anyString());

        emailService.sendEmail("gauravdha45@gmail.com", "Subject", "Hello");

        verify(emailService, times(1))
                .sendEmail(anyString(), anyString(), anyString());
    }


}
