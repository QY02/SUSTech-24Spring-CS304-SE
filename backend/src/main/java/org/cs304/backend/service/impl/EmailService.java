package org.cs304.backend.service.impl;

import jakarta.annotation.Resource;
import org.cs304.backend.exception.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.time.ZoneId;

/**
 * @author zyp
 * @date 2024/4/22 14:08
 * @description
 **/
@Service
public class EmailService {

    @Resource
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderAddress;


    /**
     * 发送邮箱验证码操作
     *
     * @param to 邮箱
     * @param title 邮件标题
     * @param content 邮件内容
     * @param dateTime 发送时间
     *
     */
    public void sendEmail(String to, String title, String content, LocalDateTime dateTime) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderAddress);
        Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        message.setSentDate(date);
        message.setSubject(title);
        message.setText(content);
        message.setTo(to);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            throw new ServiceException("Email sending failed");
        }
    }
    public void sendImmediateEmail(String to, String title, String content) {
        sendEmail(to,title,content,LocalDateTime.now());
    }


}
