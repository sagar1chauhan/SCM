// package com.scm.services;



// // public interface EmailService {

// //     void sendEmail(String to,String subject,String body);
// //     void sendEmailWithHtml();
// //     void sendEmailWithAttachment();
// // } 


// // package com.scm.services;

// import jakarta.mail.internet.MimeMessage;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.mail.javamail.MimeMessageHelper;
// import org.springframework.scheduling.annotation.Async;
// import org.springframework.stereotype.Service;

// @Service
// public class EmailService {

//     @Autowired
//     private JavaMailSender mailSender;

//     // simple text email (blocking)
//     public void sendSimpleEmail(String to, String subject, String text) {
//         SimpleMailMessage msg = new SimpleMailMessage();
//         msg.setTo(to);
//         msg.setSubject(subject);
//         msg.setText(text);
//         mailSender.send(msg);
//     }

//     // HTML email (blocking)
//     public void sendHtmlEmail(String to, String subject, String htmlBody) {
//         try {
//             MimeMessage message = mailSender.createMimeMessage();
//             MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
//             helper.setTo(to);
//             helper.setSubject(subject);
//             helper.setText(htmlBody, true); // true = isHtml
//             mailSender.send(message);
//         } catch (Exception ex) {
//             // handle/log error
//             ex.printStackTrace();
//         }
//     }

//     // ASYNC version (non-blocking)
//     @Async
//     public void sendHtmlEmailAsync(String to, String subject, String htmlBody) {
//         sendHtmlEmail(to, subject, htmlBody);
//     }
// }
package com.scm.services;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * --------------------------------------------
     *  BASIC EMAIL (used by UserServiceImpl)
     * --------------------------------------------
     */
    public void sendEmail(String to, String subject, String message) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(message);
mail.setFrom("sagarchouhan7609@gmail.com");
            mailSender.send(mail);
            log.info("Basic email sent to {}", to);
        } catch (Exception e) {
            log.error("Error sending basic email: {}", e.getMessage());
        }
    }

    /**
     * Send Simple Text Email
     */
    public void sendSimpleEmail(String to, String subject, String message) {
        sendEmail(to, subject, message); // reuse
    }

    /**
     * Send HTML Email (Blocking)
     */
    public void sendHtmlEmail(String to, String subject, String html) {
        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(message);
            log.info("HTML email sent to {}", to);

        } catch (Exception e) {
            log.error("Error sending HTML email to {}: {}", to, e.getMessage());
        }
    }

    /**
     * Send HTML Email Asynchronously
     */
    @Async
    public void sendHtmlEmailAsync(String to, String subject, String html) {
        sendHtmlEmail(to, subject, html);
    }

    /**
     * Chat Notification Email Template
     */
    public void sendChatNotificationEmail(String to, String senderName, String messageText) {

        String subject = "📩 New Chat Message from " + senderName;

        String html = """
                <div style="font-family: Arial; padding: 20px;">
                    <h3>You received a new message</h3>
                    <p><b>%s</b> sent you a new chat message:</p>
                    <div style="background:#f4f4f4; padding: 10px; border-radius: 5px;">
                        %s
                    </div>
                    <br/>
                    <a href="http://localhost:5173/chat" 
                       style="background:#0066ff; color:white; padding:10px 15px; 
                       text-decoration:none; border-radius:5px;">
                        Open Chat
                    </a>
                </div>
                """.formatted(senderName, messageText);

        sendHtmlEmailAsync(to, subject, html);
    }
}
