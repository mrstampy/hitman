package com.github.mrstampy.hit.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Loads the configuration 'custom.properties' and 'hibernate.properties' from
 * the class path, making the properties available to the application.
 * 
 * @author burton
 * 
 */
@Configuration
@PropertySource({ "classpath:custom.properties", "classpath:hibernate.properties" })
public class PropertiesConfiguration {

  /**
   * Necessary bean to allow other Spring-annotated classes to obtain a
   * property's value via <a href=
   * "http://docs.spring.io/spring/docs/4.0.2.RELEASE/spring-framework-reference/html/beans.html#beans-java-composing-configuration-classes"
   * >annotations</a>.
   * 
   * @return
   */
  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

}
