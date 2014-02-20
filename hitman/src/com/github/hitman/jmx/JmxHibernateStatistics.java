package com.github.hitman.jmx;

import javax.annotation.PostConstruct;
import javax.management.ObjectName;

import org.hibernate.SessionFactory;
import org.hibernate.jmx.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.stereotype.Component;

@SuppressWarnings("deprecation")
@Component
public class JmxHibernateStatistics {
  private static final Logger log = LoggerFactory.getLogger(JmxHibernateStatistics.class);

  @Autowired
  private SessionFactory sessionFactory;

  @Autowired
  private MBeanExporter exporter;

  @PostConstruct
  public void postConstruct() throws Exception {
    log.debug("Adding session factory stats to JMX");

    ObjectName on = new ObjectName("org.hibernate:type=Statistics");

    StatisticsService stats = new StatisticsService();
    stats.setSessionFactory(sessionFactory);
    stats.setStatisticsEnabled(true);
    
    exporter.registerManagedResource(stats, on);
  }

}
