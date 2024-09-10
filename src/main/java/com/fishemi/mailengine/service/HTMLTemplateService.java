package com.fishemi.mailengine.service;

import com.fishemi.mailengine.constant.MailButtonConstant;
import com.fishemi.mailengine.dto.CampaignEmailEventEmployeeDto;
import com.fishemi.mailengine.dto.LoginEmailEventDto;
import com.fishemi.mailengine.enumerator.TemplateNameEnum;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.Year;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class HTMLTemplateService {

  private final SpringTemplateEngine templateEngine;
  private final String webSiteUrl;
  private final String apiUrl;

  public HTMLTemplateService(
    final SpringTemplateEngine templateEngine,
    @Value("${website-url}") final String webSiteUrl,
    @Value("${api-url}") final String apiUrl
  ) {
    this.templateEngine = templateEngine;
    this.webSiteUrl = webSiteUrl;
    this.apiUrl = apiUrl;
  }

  public String getCampaignHtmlContent(
    @Nullable final UUID eventId,
    final TemplateNameEnum templateName,
    final String companyName,
    final CampaignEmailEventEmployeeDto employee,
    String htmlParagraphContent,
    final String subject
  ) {
    // create mail content
    final String logoTrackingPixelUrl = UriComponentsBuilder.fromUriString(this.apiUrl)
      .path("/assets/cdn/images/logo/100x100/" + templateName.toString().toLowerCase() + "/" + eventId + ".png")
      .toUriString();
    final String formUrl = UriComponentsBuilder.fromUriString(this.webSiteUrl)
      .path("/assets/" + companyName.replaceAll("\\s", "-").toLowerCase() + "/sso/my-account/update-credentials/" + eventId)
      .toUriString();

    String templatePath;
    String buttonContent;
    switch (templateName) {
      case TemplateNameEnum.GOOGLE -> {
        templatePath = "google.html";
        buttonContent = MailButtonConstant.GOOGLE;
      }
      case TemplateNameEnum.MICROSOFT -> {
        templatePath = "microsoft.html";
        buttonContent = MailButtonConstant.MICROSOFT;
      }
      default -> {
        templatePath = "plain.html";
        buttonContent = MailButtonConstant.PLAIN;
      }
    }

    htmlParagraphContent = htmlParagraphContent
      .replace("{{employeeName}}", employee.getFullName())
      .replace("{{boutton}}", buttonContent.formatted(formUrl))
      .replace("{{employeeEmail}}", employee.getEmail());

    final Context context = getThymeleafContext(Map.of(
      "fullName", employee.getFullName(),
      "email", employee.getEmail(),
      "subject", subject,
      "htmlParagraphContent", htmlParagraphContent,
      "logoTrackingPixelUrl", logoTrackingPixelUrl,
      "currentYear", Year.now().getValue(),
      "companyName", companyName
    ));
    return this.templateEngine.process(templatePath, context);
  }

  public String getLoginHtmlContent(final LoginEmailEventDto message) {
    final String otpUrl = UriComponentsBuilder.fromUriString(this.webSiteUrl)
      .path("/otp")
      .queryParam("email", message.getEmail())
      .queryParam("token", message.getOtpCode())
      .toUriString();

    final Context context = getThymeleafContext(Map.of("firstName", message.firstName, "otpUrl", otpUrl));
    return this.templateEngine.process("login.html", context);
  }

  public String getFishedHtmlContent(final String companyName) {
    final Context context = getThymeleafContext(Map.of("companyName", companyName));
    return this.templateEngine.process("fished.html", context);
  }

  private static Context getThymeleafContext(Map<String, Object> dynamicFields) {
    final var context = new Context();
    context.setVariables(dynamicFields);
    return context;
  }
}
