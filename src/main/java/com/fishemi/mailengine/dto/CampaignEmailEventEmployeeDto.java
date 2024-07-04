package com.fishemi.mailengine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CampaignEmailEventEmployeeDto {
  public UUID id;
  private String email;

  @JsonProperty("full_name")
  private String fullName;
}
