package com.fishemi.mailengine.controller;

import com.fishemi.mailengine.service.HTMLTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class HTMLTemplateController {

  private final HTMLTemplateService htmlTemplateService;

  public HTMLTemplateController(final HTMLTemplateService htmlTemplateService) {
    this.htmlTemplateService = htmlTemplateService;
  }

  @GetMapping("/html-render-fished")
  public ResponseEntity<String> getFishedHtmlContent(@RequestParam("company_name") String companyName) {
    log.info("HTTP handling getFishedHtmlContent, companyName={}", companyName);
    return ResponseEntity.ok(this.htmlTemplateService.getFishedHtmlContent(companyName));
  }
}
