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
package com.github.mrstampy.hit.example;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * HIT example configuration. An application using HIT would have 'n' number of
 * configuration classes, where 'n' >= 1.
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
