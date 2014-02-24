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
package com.github.mrstampy.hit.jmx;

import javax.annotation.PostConstruct;
import javax.management.ObjectName;

import org.hibernate.SessionFactory;
import org.hibernate.jmx.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.stereotype.Component;

/**
 * 
 * If JMX is enabled in the configuration, the session factory's statistics will
 * be made available via JMX.
 * 
 * @author burton
 * 
 */
@SuppressWarnings("deprecation")
@Component
public class JmxHibernateStatistics {
  private static final Logger log = LoggerFactory.getLogger(JmxHibernateStatistics.class);

  @Autowired
  private SessionFactory sessionFactory;

  @Autowired
  private MBeanExporter exporter;

  @Value("${jmx.enabled}")
  private boolean jmxEnabled;

  @PostConstruct
  public void postConstruct() throws Exception {
    if (jmxEnabled) {
      log.debug("Adding session factory stats to JMX");

      ObjectName on = new ObjectName("org.hibernate:type=Statistics");

      StatisticsService stats = new StatisticsService();
      stats.setSessionFactory(sessionFactory);
      stats.setStatisticsEnabled(true);

      exporter.registerManagedResource(stats, on);
    } else {
      log.debug("JMX disabled; Hibernate session factory statistics unavailable via JMX");
    }
  }

}
