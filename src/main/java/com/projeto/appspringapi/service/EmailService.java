package com.projeto.appspringapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void enviarEmail(String para, String assunto, String corpo, boolean isHtml) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        try {
            helper.setTo(para);
            helper.setSubject(assunto);
            helper.setText(corpo, isHtml); // true indica que o corpo é HTML
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            // Lidar com a exceção
        }
    }
}
