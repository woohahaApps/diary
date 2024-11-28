package com.woohahaapps.study.diary.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class SendMailConfig {

    @Value("${spring.mail.host}")
    public String host;

    @Value("${spring.mail.port}")
    public int port;

    @Value("${spring.mail.username}")
    public String sendEmailAddress;

    @Value("${spring.mail.password}")
    public String sendEmailPassword;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    public boolean auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    public boolean starttlsEnable;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(sendEmailAddress);
        mailSender.setPassword(sendEmailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttlsEnable);
        props.put("mail.debug", "true");

        return mailSender;
    }

}
