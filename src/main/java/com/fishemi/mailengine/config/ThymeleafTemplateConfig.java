package com.fishemi.mailengine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.nio.charset.StandardCharsets;

@Configuration
public class ThymeleafTemplateConfig {

  public ClassLoaderTemplateResolver classLoaderTemplateResolver() {
    final var classLoaderTemplateResolver = new ClassLoaderTemplateResolver();
    classLoaderTemplateResolver.setPrefix("/templates/");
    classLoaderTemplateResolver.setSuffix(".html");
    classLoaderTemplateResolver.setTemplateMode(TemplateMode.HTML);
    classLoaderTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
    classLoaderTemplateResolver.setCacheable(false);
    return classLoaderTemplateResolver;
  }

  @Bean
  public SpringTemplateEngine springTemplateEngine() {
    final var springTemplateEngine = new SpringTemplateEngine();
    springTemplateEngine.addTemplateResolver(classLoaderTemplateResolver());
    return springTemplateEngine;
  }
}
