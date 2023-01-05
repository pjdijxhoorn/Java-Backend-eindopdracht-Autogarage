package com.example.garage.Repositories;

import com.example.garage.Models.Email;

public interface EmailRepository {
    String sendSimpleMail(Email details);
    String sendMailWithAttachment(Email details);
}
