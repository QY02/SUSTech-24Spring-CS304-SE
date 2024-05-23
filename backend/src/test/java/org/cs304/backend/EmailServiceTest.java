package org.cs304.backend;

/**
 * @author zyp
 * @date 2024/5/23 22:58
 * @description
 **/
import org.cs304.backend.service.impl.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    public void testSendImmediateEmail() {
        String to = "recipient@example.com";
        String title = "Test Email";
        String content = "This is a test email content.";

        // Mocking JavaMailSender and do nothing when send method is called
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // Calling the method under test
        emailService.sendImmediateEmail(to, title, content);

        // Verify that the send method of JavaMailSender is called once
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
