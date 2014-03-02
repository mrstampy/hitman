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

import java.sql.Connection;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.stereotype.Component;
import org.vibur.dbcp.ViburDBCPDataSource;
import org.vibur.dbcp.listener.DestroyListener;
import org.vibur.dbcp.pool.PoolOperations;

/**
 * Returns a <a href="https://code.google.com/p/vibur-dbcp/">Vibur DBCP</a> data
 * source.
 * 
 * @author burton
 * 
 */
@Component
public class ViburDBCPDataSourceCreator extends AbstractDataSourceCreator<LazyConnectionDataSourceProxy> {
  private static final Logger log = LoggerFactory.getLogger(ViburDBCPDataSourceCreator.class);

  @Override
  protected LazyConnectionDataSourceProxy createDataSource(Properties props) {
    return new LazyConnectionDataSourceProxy(createViburDataSource(props));
  }

  private ViburDBCPDataSource createViburDataSource(Properties props) {
    ViburDBCPDataSource dataSource = null;
    if (props == null) {
      dataSource = new ViburDBCPDataSource();
      dataSource.setPoolInitialSize(minIdle);
      dataSource.setPoolMaxSize(maxActive);
    } else {
      dataSource = new ViburDBCPDataSource(props);
    }

    dataSource.setJdbcUrl(jdbcUrl);
    dataSource.setUsername(username);
    dataSource.setPassword(password);

    dataSource.setPoolOperations(new PoolOperations(dataSource, new DestroyListener() {

      @Override
      public void onDestroy(Connection connection) {
        log.trace("Vibur DBCP connection destroyed");
      }
    }));

    return dataSource;
  }

  @Override
  public void init() throws Exception {
    
  }

}
