package com.fishemi.mailengine.service;

import com.fishemi.mailengine.dto.CampaignEmailEventDto;
import com.fishemi.mailengine.dto.LoginEmailEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMqReceiverService {

  private final MailingService mailingService;

  public RabbitMqReceiverService(final MailingService mailingService) {
    this.mailingService = mailingService;
  }

  @RabbitListener(queues = "${campaign-email-event-queue}", messageConverter = "jackson2JsonMessageConverter")
  public void receiveCampaignEmailEventQueue(final CampaignEmailEventDto message) {
    try {
      log.info("Receive campaign email event, message={}", message);
      this.mailingService.handleCampaignEmailEventQueue(message);
      log.info("Campaign email successfully sent to all employees, size={}", message.getEmployees().size());
    } catch (Exception e) {
      log.error("Error happens while sending campaign email", e);
    }
  }

  @RabbitListener(queues = "${login-email-event-queue}", messageConverter = "jackson2JsonMessageConverter")
  public void receiveLoginEmailEventQueue(final LoginEmailEventDto message) {
    try {
      log.info("Receive login email event, message={}", message);
      this.mailingService.handleLoginEmailEventQueue(message);
      log.info("Login email has been successfully, email={}", message.email);
    } catch (Exception e) {
      log.error("Error happens while sending login email", e);
    }
  }
}
