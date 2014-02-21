package com.github.mrstampy.hit.spring.config;

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
 * Configuration class enabling <a href=
 * "http://docs.spring.io/spring/docs/4.0.2.RELEASE/spring-framework-reference/html/jmx.html"
 * >JMX via Spring</a>. Important information about JMX can be found on <a href=
 * "http://docs.oracle.com/javase/6/docs/technotes/guides/management/agent.html"
 * >the Oracle site</a>.<br>
 * <br>
 * Add the following to the startup parameters of your application to enable JMX.  These are
 * a subset of the parameters available; refer to the <a href=
 * "http://docs.oracle.com/javase/6/docs/technotes/guides/management/agent.html"
 * >Oracle documentation</a> for a complete list.<br>
 * <br>
 * <ol>
 * <li><b>-Dcom.sun.management.jmxremote</b> - necessary to enable JMX</li>
 * <li><b>-Dcom.sun.management.jmxremote.port=9999</b> - good form to specify the port on which to connect</li>
 * <li><b>-Dcom.sun.management.jmxremote.local.only=true</b> - allows only local JMX connections</b></li>
 * </ol><br>
 * <br>
 * If connecting remotely (local.only=false), the URL to use is of the following form:<br>
 * <br>
 * <b>service:jmx:rmi:///jndi/rmi://[hostName]:[portNum]/jmxrmi</b>
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
