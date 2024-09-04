package com.fishemi.mailengine.service;

import com.fishemi.mailengine.dto.CampaignEmailEventDto;
import com.fishemi.mailengine.dto.CampaignEmailEventEmployeeDto;
import com.fishemi.mailengine.dto.LoginEmailEventDto;
import com.fishemi.mailengine.entity.EventEntity;
import com.fishemi.mailengine.enumerator.TemplateNameEnum;
import com.fishemi.mailengine.repository.EventRepository;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class MailingService {
  private final MailSenderService mailSenderService;
  private final EventRepository eventRepository;

  public MailingService(
    final MailSenderService mailSenderService,
    final EventRepository eventRepository
  ) {
    this.mailSenderService = mailSenderService;
    this.eventRepository = eventRepository;
  }

  public void handleCampaignEmailEventQueue(final CampaignEmailEventDto message) {
    message.getEmployees().forEach(employee -> {
      // event id is also used as a tracking pixel id
      final UUID eventId = UUID.randomUUID();

      final String htmlContent = this.mailSenderService.getCampaignHtmlContent(
        eventId,
        message.getTemplateName(),
        message.getCompanyName(),
        employee,
        message.getHtmlParagraphContent(),
        message.getSubject()
      );
      try {
        this.mailSenderService.sendEmail(employee.getEmail(), message.getSubject(), htmlContent);
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
    final String htmlContent = this.mailSenderService.getLoginHtmlContent(message);
    this.mailSenderService.sendEmail(message.email, "Votre lien de connexion Fishemi", htmlContent);
  }
}
