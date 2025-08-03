package com.oak.finance_manager.service;

import com.oak.finance_manager.exceptions.NotSendMailException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;


@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;


    @Value("${api.frontend.host}")
    private String frontendHost;

    @Value("${mail.from.name}")
    private String mailFromName;

    @Value("${spring.mail.username}")
    private String fromEmail;


    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;

        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML");
        resolver.setCharacterEncoding("UTF-8");

        this.templateEngine = new SpringTemplateEngine();
        this.templateEngine.setTemplateResolver(resolver);
    }

    public void sendActivationEmail(String token, String recipientEmail) {
        String link = frontendHost + "/activate-account?token=" + token;

        Context context = new Context();
        context.setVariable("activationLink", link);
        String htmlContent = templateEngine.process("activation-email", context);

        sendHtmlEmail(recipientEmail, "Ative sua conta - Finance Manager", htmlContent);
    }

    public void sendPasswordRecoveryEmail(String token, String recipientEmail) {
        String link = frontendHost + "/reset-password?token=" + token;

        Context context = new Context();
        context.setVariable("resetLink", link);
        String htmlContent = templateEngine.process("password-recovery-email", context);

        sendHtmlEmail(recipientEmail, "Recupere sua senha - Finance Manager", htmlContent);
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, mailFromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new NotSendMailException("Error sending email");
        }
    }
}
