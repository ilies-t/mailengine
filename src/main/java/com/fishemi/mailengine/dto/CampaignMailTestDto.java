package com.fishemi.mailengine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fishemi.mailengine.enumerator.TemplateNameEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignMailTestDto {

  @JsonProperty("template_name")
  private TemplateNameEnum templateName;

  @JsonProperty("company_name")
  private String companyName;

  private CampaignEmailEventEmployeeDto employee;

  private String subject;

  @JsonProperty("html_paragraph_content")
  private String htmlParagraphContent;
}
