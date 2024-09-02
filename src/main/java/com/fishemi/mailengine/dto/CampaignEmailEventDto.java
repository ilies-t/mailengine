package com.fishemi.mailengine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fishemi.mailengine.enumerator.TemplateNameEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignEmailEventDto {
  @JsonProperty("campaign_id")
  private UUID campaignId;

  @JsonProperty("template_name")
  private TemplateNameEnum templateName;

  @JsonProperty("company_name")
  private String companyName;

  @JsonProperty("html_paragraph_content")
  private String htmlParagraphContent;

  private String subject;

  private List<CampaignEmailEventEmployeeDto> employees;
}
