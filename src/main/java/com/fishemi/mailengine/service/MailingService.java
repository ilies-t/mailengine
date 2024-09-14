package com.fishemi.mailengine.service;

import com.fishemi.mailengine.dto.CampaignEmailEventDto;
import com.fishemi.mailengine.dto.CampaignEmailEventEmployeeDto;
import com.fishemi.mailengine.dto.LoginEmailEventDto;
import com.fishemi.mailengine.entity.EventEntity;
import com.fishemi.mailengine.enumerator.TemplateNameEnum;
import com.fishemi.mailengine.repository.EventRepository;
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
import java.util.UUID;

@Service
@Slf4j
public class MailingService {
  private final HTMLTemplateService htmlTemplateService;
  private final EventRepository eventRepository;
  private final JavaMailSender emailSender;
  private final String emailFrom;

  public MailingService(
    final HTMLTemplateService htmlTemplateService,
    final EventRepository eventRepository,
    final JavaMailSender emailSender,
    @Value("${email-from}") final String emailFrom
  ) {
    this.htmlTemplateService = htmlTemplateService;
    this.eventRepository = eventRepository;
    this.emailSender = emailSender;
    this.emailFrom = emailFrom;
  }

  public void handleCampaignEmailEventQueue(final CampaignEmailEventDto message) {
    message.getEmployees().forEach(employee -> {
      // event id is also used as a tracking pixel id
      final UUID eventId = UUID.randomUUID();

      final String htmlContent = this.htmlTemplateService.getCampaignHtmlContent(
        eventId,
        message.getTemplateName(),
        message.getCompanyName(),
        employee,
        message.getHtmlParagraphContent(),
        message.getSubject()
      );
      try {
        this.sendEmail(employee.getEmail(), message.getSubject(), htmlContent);
      } catch (MessagingException e) {
        log.error("Error happens while sending campaign email to employee, employee={}", employee, e);
      }

      final var event = EventEntity.builder()
        .id(eventId)
        .userId(UUID.fromString(employee.getId()))
        .campaignId(message.getCampaignId())
        .eventType("sent")
        .build();
      this.eventRepository.save(event);
    });
  }

  public void handleLoginEmailEventQueue(final LoginEmailEventDto message) throws MessagingException {
    final String htmlContent = this.htmlTemplateService.getLoginHtmlContent(message);
    this.sendEmail(message.email, "Votre lien de connexion Fishemi", htmlContent);
  }

  private void sendEmail(final String toEmail, final String subject, final String body) throws MessagingException {
    final MimeMessage mimeMessage = this.emailSender.createMimeMessage();
    final var mail = new MimeMessageHelper(mimeMessage, "utf-8");

    mail.setText(body, true);
    mail.setTo(toEmail);
    mail.setSubject(subject);
    mail.setFrom(this.emailFrom);

    this.emailSender.send(mimeMessage);
    log.info("Message sent successfully, toEmail={}, subject={}", toEmail, subject);
  }
}
