package com.fishemi.mailengine.controller;

import com.fishemi.mailengine.dto.HealthDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HealthController {

  @GetMapping("/")
  public ResponseEntity<HealthDto> health() {
    log.info("HTTP handling health");
    return ResponseEntity.ok(HealthDto.ok());
  }
}
