package com.github.mrstampy.hitman.spring.config.datasource;

import java.beans.PropertyVetoException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0DataSourceCreator extends AbstractDataSourceCreator<ComboPooledDataSource> {
  private static final Logger log = LoggerFactory.getLogger(C3P0DataSourceCreator.class);

  @Override
  protected ComboPooledDataSource createDataSource(Properties props) {
    ComboPooledDataSource dataSource = new ComboPooledDataSource();

    if (props == null) {
      dataSource.setMaxPoolSize(maxActive);
      dataSource.setMinPoolSize(minIdle);
    } else {
      dataSource.setProperties(props);
    }

    try {
      dataSource.setDriverClass(jdbcDriver);
    } catch (PropertyVetoException e) {
      log.error("{} is not a valid jdbc driver", jdbcDriver, e);
      throw new RuntimeException(e);
    }

    dataSource.setJdbcUrl(jdbcUrl);
    dataSource.setUser(username);
    dataSource.setPassword(password);

    return dataSource;
  }

}
