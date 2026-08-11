package com.laptrinhfulllstack.commonservice.services;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j // Thêm annotation này để dùng được biến log
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Configuration config;

    /**
     * Gửi email hỗ trợ HTML và đính kèm file
     * 
     * @param to         Email người nhận
     * @param subject    Tiêu đề email
     * @param text       Nội dung email (có thể là HTML hoặc text thuần)
     * @param isHtml     True nếu nội dung là HTML
     * @param attachment File đính kèm (nếu có, có thể để null)
     */
    public void sendEmail(String to, String subject, String text, boolean isHtml, File attachment) {
        try {
            // Sử dụng MimeMessage để hỗ trợ gửi định dạng HTML và file đính kèm
            MimeMessage message = javaMailSender.createMimeMessage();

            // Tham số true nghĩa là cho phép multipart (đính kèm file)
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, isHtml);

            // Nếu có file đính kèm và file tồn tại thì thêm vào email
            if (attachment != null && attachment.exists()) {
                helper.addAttachment(attachment.getName(), attachment);
            }

            javaMailSender.send(message);
            log.info("Email sent successfully to {}", to);

        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
            // Có thể quăng RuntimeException nếu muốn bắt lỗi ở tầng trên
        }
    }

    /**
     * Gửi email hỗ trợ HTML và đính kèm file
     * 
     * @param to         Email người nhận
     * @param subject    Tiêu đề email
     * @param text       Nội dung email (có thể là HTML hoặc text thuần)
     * @param isHtml     True nếu nội dung là HTML
     * @param attachment File đính kèm (nếu có, có thể để null)
     */
    public void sendEmailWithTemplate(String to, String subject, String templateName, Map<String, Object> placeholders,
            File attachment) {
        try {
            Template t = config.getTemplate(templateName);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, placeholders);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            if (attachment != null && attachment.exists()) {
                helper.addAttachment(attachment.getName(), attachment);
            }

            javaMailSender.send(message);
            log.info("Email sent successfully to {}", to);

        } catch (MessagingException | IOException | TemplateException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
        }
    }
}