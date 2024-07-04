package com.fishemi.mailengine.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class HealthDto {
  public String status;

  public static HealthDto ok() {
    return new HealthDto("OK");
  }
}
