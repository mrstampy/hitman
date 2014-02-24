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

  /**
   * Always used by implementations to define the username
   */
  @Value("${hibernate.connection.username}")
  protected String username;

  /**
   * Always used by implementations to define the password
   */
  @Value("${hibernate.connection.password}")
  protected String password;

  /**
   * Always used by implementations to define the JDBC URL
   */
  @Value("${hibernate.connection.url}")
  protected String jdbcUrl;

  /**
   * Always used by implementations to define the JDBC driver class
   */
  @Value("${hibernate.connection.driver_class}")
  protected String jdbcDriver;

  /**
   * Used by implementations to define the maximum size of the connection pool
   * if the 'datasource.property.file' property has not been defined.
   */
  @Value("${max.active.connections}")
  protected int maxActive;

  /**
   * Used by implementations to define the minimum size of the connection pool
   * if the 'datasource.property.file' property has not been defined.
   */
  @Value("${min.idle.connections}")
  protected int minIdle;

  /**
   * If specified it must be the name of the properties file on the classpath
   * with the datasource-specific properties. Hibernate properties for
   * connection will always be used regardless of values specified in such a
   * file.
   */
  @Value("${datasource.property.file}")
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

  /**
   * Subclasses implement the creation of the datasource. The properties object
   * may be null.
   * 
   * @param properties
   * @return
   * @throws Exception
   */
  protected abstract DS createDataSource(Properties properties) throws Exception;

  private Properties getDataSourceProperties() {
    if (isEmpty(dataSourceProperties)) {
      log.info("Configuring datasource with default properties");
      return null;
    }

    Properties props = new Properties();

    try {
      props.load(getClass().getResourceAsStream("/" + dataSourceProperties));
      log.info("Configuring datasource with properties {}", dataSourceProperties);
      return props;
    } catch (Exception e) {
      log.error("Could not load {}, using defaults", dataSourceProperties, e);
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
