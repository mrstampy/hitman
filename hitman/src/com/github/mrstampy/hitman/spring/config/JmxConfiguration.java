package com.github.mrstampy.hitman.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource;
import org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler;
import org.springframework.jmx.export.metadata.JmxAttributeSource;
import org.springframework.jmx.export.naming.MetadataNamingStrategy;
import org.springframework.jmx.export.naming.ObjectNamingStrategy;
import org.springframework.jmx.support.ConnectorServerFactoryBean;
import org.springframework.jmx.support.MBeanServerFactoryBean;

/**
 * IMPORTANT: Add '-Dcom.sun.management.jmxremote' to the startup VM parms to
 * enable JMX. Use JConsole to connect.
 * 
 */
@Configuration
@EnableMBeanExport(defaultDomain = "${default.jmx.domain}")
public class JmxConfiguration {
  private static final Logger log = LoggerFactory.getLogger(JmxConfiguration.class);

  @Autowired
  private JmxAttributeSource jmxAttributeSource;

  @Autowired
  private MBeanServerFactoryBean server;

  @Autowired
  private MetadataMBeanInfoAssembler assembler;

  @Autowired
  private ObjectNamingStrategy namingStrategy;

  @Bean
  public JmxAttributeSource jmxAttributeSource() {
    log.debug("Creating jmx attribute source");
    return new AnnotationJmxAttributeSource();
  }

  @Bean
  public ConnectorServerFactoryBean connectorServerFactoryBean() {
    log.debug("Creating connector server factory bean");
    ConnectorServerFactoryBean bean = new ConnectorServerFactoryBean();

    bean.setDaemon(true);

    return bean;
  }

  @Bean
  public MBeanServerFactoryBean mbeanServerFactoryBean() {
    log.debug("Creating mbean server factory bean");
    MBeanServerFactoryBean bean = new MBeanServerFactoryBean();

    bean.setRegisterWithFactory(true);

    return bean;
  }

  @Bean
  public MetadataMBeanInfoAssembler metadataMBeanInfoAssembler() {
    log.debug("Creating metadata mbean info assembler");
    return new MetadataMBeanInfoAssembler(jmxAttributeSource);
  }

  @Bean
  public ObjectNamingStrategy objectNamingStrategy() {
    log.debug("Creating object naming strategy");
    return new MetadataNamingStrategy(jmxAttributeSource);
  }
}
