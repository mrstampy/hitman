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

import java.beans.PropertyVetoException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Returns a <a href="http://www.mchange.com/projects/c3p0/">C3P0</a>
 * datasource.
 * 
 * @author burton
 * 
 */
@Component
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

  @Override
  public void init() {
    
  }

}
