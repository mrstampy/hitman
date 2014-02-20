package com.github.mrstampy.hitman.spring.config.datasource;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.support.MBeanServerFactoryBean;

public class TomcatJDBCDataSourceCreator extends AbstractDataSourceCreator<DataSource> {
  private static final Logger log = LoggerFactory.getLogger(TomcatJDBCDataSourceCreator.class);

  @Autowired
  private MBeanServerFactoryBean server;

  @PostConstruct
  public void postConstruct() throws Exception {
    log.debug("Registering Tomcat JDBC datasource with JMX");

    DataSource ds = dataSource();
    if (!ds.isJmxEnabled()) {
      log.debug("JMX disabled on Tomcat JDBC datasource");
      return;
    }

    MBeanServer mb = server.getObject();

    ds.createPool();

    ds.preRegister(mb, new ObjectName("org.apache.tomcat:context=HibernateDataSource"));
  }

  protected DataSource createDataSource(Properties props) {
    DataSource dataSource = new DataSource();

    if (props == null) {
      dataSource.setMaxActive(maxActive);
      dataSource.setMinIdle(minIdle);
      dataSource.setJmxEnabled(jmxEnabled);
    } else {
      dataSource.setPoolProperties(getPoolConfig(props));
    }

    dataSource.setDriverClassName(jdbcDriver);
    dataSource.setUrl(jdbcUrl);
    dataSource.setUsername(username);
    dataSource.setPassword(password);

    return dataSource;
  }

  private PoolConfiguration getPoolConfig(Properties props) {
    PoolConfiguration config = new PoolProperties();

    config.setDbProperties(props);

    return config;
  }

}
