package com.fishemi.mailengine.service;

import com.fishemi.mailengine.dto.CampaignEmailEventEmployeeDto;
import com.fishemi.mailengine.dto.LoginEmailEventDto;
import com.fishemi.mailengine.enumerator.TemplateNameEnum;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
@Slf4j
public class MailSenderService {

  private final JavaMailSender emailSender;
  private final SpringTemplateEngine templateEngine;
  private final String emailFrom;
  private final String webSiteLoginUrl;

  public MailSenderService(
    final JavaMailSender emailSender,
    final SpringTemplateEngine templateEngine,
    @Value("${email-from}") final String emailFrom,
    @Value("${website-login-url}") final String webSiteLoginUrl
  ) {
    this.templateEngine = templateEngine;
    this.emailSender = emailSender;
    this.emailFrom = emailFrom;
    this.webSiteLoginUrl = webSiteLoginUrl;
  }

  public void sendEmail(final String toEmail, final String subject, final String body) throws MessagingException {
    final MimeMessage mimeMessage = this.emailSender.createMimeMessage();
    final var mail = new MimeMessageHelper(mimeMessage, "utf-8");

    mail.setText(body, true);
    mail.setTo(toEmail);
    mail.setSubject(subject);
    mail.setFrom(this.emailFrom);

    this.emailSender.send(mimeMessage);
    log.info("Message sent successfully, toEmail={}, subject={}", toEmail, subject);
  }

  public String getCampaignHtmlContent(
    final TemplateNameEnum templateName,
    final CampaignEmailEventEmployeeDto employee
  ) {
    final Context context = getThymeleafContext(Map.of(
      "fullName", employee.getFullName(), "email", employee.getEmail()
    ));
    return switch (templateName) {
      case TemplateNameEnum.GOOGLE -> this.templateEngine.process("google.html", context);
      case TemplateNameEnum.MICROSOFT -> this.templateEngine.process("microsoft.html", context);
      default -> this.templateEngine.process("plain.html", context);
    };
  }

  public String getLoginHtmlContent(final LoginEmailEventDto message) {
    final String otpUrl = UriComponentsBuilder.fromUriString(this.webSiteLoginUrl)
      .queryParam("email", message.getEmail())
      .queryParam("token", message.getOtpCode())
      .toUriString();

    final Context context = getThymeleafContext(Map.of("firstName", message.firstName, "otpUrl", otpUrl));
    return this.templateEngine.process("login.html", context);
  }

  public static Context getThymeleafContext(Map<String, Object> dynamicFields) {
    final var context = new Context();
    context.setVariables(dynamicFields);
    return context;
  }
}
