package com.mchis.LearningPlatform.services;

import com.mchis.LearningPlatform.entities.EmailDetails;

public interface EmailService {
    String sendSimpleMail(EmailDetails details);
    String sendMailWithAttachment(EmailDetails details);
}
