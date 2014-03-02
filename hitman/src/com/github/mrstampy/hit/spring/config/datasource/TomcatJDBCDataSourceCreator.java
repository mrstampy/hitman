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

import java.util.Properties;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import org.springframework.stereotype.Component;

/**
 * Returns a <a
 * href="https://people.apache.org/~fhanik/jdbc-pool/jdbc-pool.html">Tomcat
 * JDBC</a> datasource. If enabled, the datasource's properties and methods will
 * be available via JMX.
 * 
 * @author burton
 * 
 */
@Component
public class TomcatJDBCDataSourceCreator extends AbstractDataSourceCreator<DataSource> {
  private static final Logger log = LoggerFactory.getLogger(TomcatJDBCDataSourceCreator.class);

  @Autowired
  private MBeanServerFactoryBean server;

  public void init() throws Exception {
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
