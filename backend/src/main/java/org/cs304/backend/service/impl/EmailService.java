package org.cs304.backend.service.impl;

import jakarta.annotation.Resource;
import org.cs304.backend.exception.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

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
     * @param date 发送时间
     *
     */
    public void sendEmail(String to, String title, String content, Date date) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderAddress);
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
        sendEmail(to,title,content,new Date());
    }


}
