package com.example.garage.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.mail.internet.MimeBodyPart;
import java.io.File;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {

    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;

    public Email(String recipient, String msgBody, String subject) {
        this.recipient = recipient;
        this.msgBody = msgBody;
        this.subject = subject;
    }

}
