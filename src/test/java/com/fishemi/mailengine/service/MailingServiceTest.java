package com.fishemi.mailengine.service;

import com.fishemi.mailengine.dto.CampaignEmailEventDto;
import com.fishemi.mailengine.dto.CampaignEmailEventEmployeeDto;
import com.fishemi.mailengine.entity.EventEntity;
import com.fishemi.mailengine.enumerator.TemplateNameEnum;
import com.fishemi.mailengine.repository.EventRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
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

  @InjectMocks
  public MailingService mailingService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void handleCampaignEmailEventQueue_test() throws MessagingException {
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

    // when
    Mockito.doNothing().when(this.emailSender).send((MimeMessage) Mockito.any());
    Mockito.when(this.htmlTemplateService.getCampaignHtmlContent(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
      .thenReturn("<h1>Title</h1>");
    Mockito.when(this.eventRepository.save(Mockito.any())).thenReturn(new EventEntity());
    this.mailingService.handleCampaignEmailEventQueue(message);

    // then
    Mockito.verify(eventRepository, Mockito.times(3)).save(Mockito.any());
  }
}
