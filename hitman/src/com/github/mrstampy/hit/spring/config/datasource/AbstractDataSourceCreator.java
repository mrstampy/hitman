package com.github.mrstampy.hit.spring.config.datasource;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public abstract class AbstractDataSourceCreator<DS extends DataSource> implements DataSourceCreator<DS> {
  private static final Logger log = LoggerFactory.getLogger(AbstractDataSourceCreator.class);

  @Value("${hibernate.connection.username}")
  protected String username;

  @Value("${hibernate.connection.password}")
  protected String password;

  @Value("${hibernate.connection.url}")
  protected String jdbcUrl;

  @Value("${hibernate.connection.driver_class}")
  protected String jdbcDriver;

  @Value("${max.active.connections}")
  protected int maxActive;

  @Value("${min.idle.connections}")
  protected int minIdle;

  @Value("${datasource.properties}")
  protected String dataSourceProperties;

  @Value("${jmx.enabled}")
  protected boolean jmxEnabled;

  private Lock lock = new ReentrantLock(true);

  private DS dataSource;

  @Override
  @Bean
  public final DS dataSource() {
    if (getDataSource() == null) {
      lock.lock();
      try {
        if (getDataSource() == null) {
          initDataSource();
        }
      } finally {
        lock.unlock();
      }
    }

    return getDataSource();
  }

  private void initDataSource() {
    try {
      setDataSource(createDataSource(getDataSourceProperties()));
    } catch (Exception e) {
      log.error("Unexpected exception creating datasource", e);
    }
  }

  protected abstract DS createDataSource(Properties properties) throws Exception;

  private Properties getDataSourceProperties() {
    if (isEmpty(dataSourceProperties)) {
      return null;
    }

    Properties props = new Properties();

    try {
      props.load(getClass().getResourceAsStream(dataSourceProperties));
      return props;
    } catch (Exception e) {
      log.error("Could not load {}", dataSourceProperties, e);
    }

    return null;
  }

  protected DS getDataSource() {
    return dataSource;
  }

  protected void setDataSource(DS dataSource) {
    this.dataSource = dataSource;
  }

}
