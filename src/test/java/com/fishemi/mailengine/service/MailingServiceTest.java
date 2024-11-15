package com.fishemi.mailengine.service;

import com.fishemi.mailengine.dto.CampaignEmailEventDto;
import com.fishemi.mailengine.dto.CampaignEmailEventEmployeeDto;
import com.fishemi.mailengine.entity.EventEntity;
import com.fishemi.mailengine.enumerator.TemplateNameEnum;
import com.fishemi.mailengine.repository.EventRepository;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;
import java.util.UUID;

class MailingServiceTest {

  @Mock
  public EventRepository eventRepository;

  @Mock
  public HTMLTemplateService htmlTemplateService;

  @Mock
  public JavaMailSender emailSender;

  public MailingService mailingService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    this.mailingService = new MailingService(
      this.htmlTemplateService,
      this.eventRepository,
      this.emailSender,
      "test@fishemi.com"
    );
  }

  @Test
  void handleCampaignEmailEventQueue_test() {
    // given
    final List<CampaignEmailEventEmployeeDto> employees = List.of(
      CampaignEmailEventEmployeeDto.builder().id(UUID.randomUUID().toString()).email("email-1@gmail.com").fullName("Email 1").build(),
      CampaignEmailEventEmployeeDto.builder().id(UUID.randomUUID().toString()).email("email-2@gmail.com").fullName("Email 2").build(),
      CampaignEmailEventEmployeeDto.builder().id(UUID.randomUUID().toString()).email("email-3@gmail.com").fullName("Email 3").build()
    );
    final CampaignEmailEventDto message = new CampaignEmailEventDto();
    message.setCampaignId(UUID.randomUUID());
    message.setTemplateName(TemplateNameEnum.GOOGLE);
    message.setEmployees(employees);
    message.setSubject("Subject");

    // when
    final var mimeMessage = new MimeMessage((Session)null);
    Mockito.when(this.emailSender.createMimeMessage()).thenReturn(mimeMessage);
    Mockito.doNothing().when(this.emailSender).send((MimeMessage) Mockito.any());
    Mockito.when(this.htmlTemplateService.getCampaignHtmlContent(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
      .thenReturn("<h1>This is a title</h1>");
    Mockito.when(this.eventRepository.save(Mockito.any())).thenReturn(new EventEntity());
    this.mailingService.handleCampaignEmailEventQueue(message);

    // then
    Mockito.verify(eventRepository, Mockito.times(3)).save(Mockito.any());
  }
}
