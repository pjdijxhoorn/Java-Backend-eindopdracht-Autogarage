package com.example.garage.Services;

import com.lowagie.text.Document;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;


@Service
public class EmailService {

   private final JavaMailSender emailsender;

   public EmailService(JavaMailSender emailsender){
       this.emailsender = emailsender;
   }

   void sendMessage(String to, String subject, String text){
       SimpleMailMessage message = new SimpleMailMessage();
       message.setFrom("garagetransparant@gmail.com");
       message.setTo(to);
       message.setSubject(subject);
       message.setText(text);
       this.emailsender.send(message);
   }

    /*public void sendMessageWithAttach(String to, String subject, String text, String fileToAttach) throws MessagingException {
        MimeMessage message = emailsender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("garagetransparant@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);
        FileSystemResource file = new FileSystemResource(new File(fileToAttach));
        helper.addAttachment("Invoice", file);


        this.emailsender.send(message);
    }*/


}
