package com.github.mrstampy.hit.example;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * An application using HIT would have 'n' number of configuration classes,
 * where 'n' >= 1.
 * 
 * @author burton
 * 
 */
@Configuration
@ComponentScan("com.github.mrstampy.hit.example")
public class HitConfiguration {
  private static final Logger log = LoggerFactory.getLogger(HitConfiguration.class);

  @Value("${my.example.property}")
  private String myExampleProperty;

  @PostConstruct
  public void postConstruct() throws Exception {
    log.info("My example property is {}", myExampleProperty);
  }
}
