package com.fishemi.mailengine.controller;

import com.fishemi.mailengine.dto.CampaignEmailEventEmployeeDto;
import com.fishemi.mailengine.dto.LoginEmailEventDto;
import com.fishemi.mailengine.enumerator.TemplateNameEnum;
import com.fishemi.mailengine.service.MailSenderService;
import com.fishemi.mailengine.service.MailingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@Slf4j
public class MailTestController {

  private final MailSenderService mailSenderService;

  public MailTestController(final MailSenderService mailSenderService) {
    this.mailSenderService = mailSenderService;
  }

  @PostMapping("/login-email-test")
  public ResponseEntity<String> getLoginMailTest(@RequestBody LoginEmailEventDto loginEmailEventDto) {
    log.info("HTTP handling getLoginMailTest, loginEmailEventDto={}", loginEmailEventDto);
    return ResponseEntity.ok(this.mailSenderService.getLoginHtmlContent(loginEmailEventDto));
  }

  @PostMapping("/campaign-email-test")
  public ResponseEntity<String> getCampaignMailTest(
    @RequestParam TemplateNameEnum templateName,
    @RequestBody CampaignEmailEventEmployeeDto campaignEmailEventEmployeeDto
  ) {
    log.info("HTTP handling getCampaignMailTest, templateName={}, loginEmailEventDto={}",
      templateName, campaignEmailEventEmployeeDto);
    return ResponseEntity.ok(this.mailSenderService.getCampaignHtmlContent(
      UUID.randomUUID(),
      templateName,
      "My company name",
      campaignEmailEventEmployeeDto,
      "text content",
      "Email subject here"
    ));
  }
}
