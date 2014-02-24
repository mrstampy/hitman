/*
 * HIT - Hibernate Induction Trigger - A Hibernate Quickstart Library 
 *
 * Copyright (C) 2014 Burton Alexander
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 * 
 */
package com.github.mrstampy.hit.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Loads the configuration 'hit.properties' and 'hibernate.properties' from
 * the class path, making the properties available to the application.
 * 
 * @author burton
 * 
 */
@Configuration
@PropertySource({ "classpath:hit.properties", "classpath:hibernate.properties" })
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
