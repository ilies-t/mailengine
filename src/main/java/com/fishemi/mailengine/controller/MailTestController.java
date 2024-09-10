package com.fishemi.mailengine.controller;

import com.fishemi.mailengine.dto.CampaignMailTestDto;
import com.fishemi.mailengine.dto.LoginEmailEventDto;
import com.fishemi.mailengine.service.HTMLTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@Slf4j
public class MailTestController {

  private final HTMLTemplateService htmlTemplateService;

  public MailTestController(final HTMLTemplateService htmlTemplateService) {
    this.htmlTemplateService = htmlTemplateService;
  }

  @PostMapping("/login-email-test")
  public ResponseEntity<String> getLoginMailTest(@RequestBody LoginEmailEventDto loginEmailEventDto) {
    log.info("HTTP handling getLoginMailTest, loginEmailEventDto={}", loginEmailEventDto);
    return ResponseEntity.ok(this.htmlTemplateService.getLoginHtmlContent(loginEmailEventDto));
  }

  @PostMapping("/campaign-email-test")
  public ResponseEntity<String> getCampaignMailTest(@RequestBody CampaignMailTestDto body) {
    log.info("HTTP handling getCampaignMailTest, companyName={}, subject={}", body.getCompanyName(), body.getSubject());
    return ResponseEntity.ok(this.htmlTemplateService.getCampaignHtmlContent(
      null,
      body.getTemplateName(),
      body.getCompanyName(),
      body.getEmployee(),
      body.getHtmlParagraphContent(),
      body.getSubject()
    ));
  }
}
