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

import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.stereotype.Component;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;

/**
 * Returns a <a href="http://jolbox.com/">BoneCP</a> datasource.
 * 
 * @author burton
 * 
 */
@Component
public class BoneCPDataSourceCreator extends AbstractDataSourceCreator<LazyConnectionDataSourceProxy> {
  
  public BoneCPDataSourceCreator() {
    System.out.println("Bone'd");
  }

  @Override
  protected LazyConnectionDataSourceProxy createDataSource(Properties props) throws Exception {
    return new LazyConnectionDataSourceProxy(createBoneCPDataSource(props));
  }

  private BoneCPDataSource createBoneCPDataSource(Properties props) throws Exception {
    BoneCPDataSource dataSource = null;

    if (props == null) {
      dataSource = new BoneCPDataSource();
      dataSource.setMaxConnectionsPerPartition(maxActive);
      dataSource.setMinConnectionsPerPartition(minIdle);
    } else {
      BoneCPConfig config = new BoneCPConfig(props);
      dataSource = new BoneCPDataSource(config);
    }

    dataSource.setJdbcUrl(jdbcUrl);
    dataSource.setDriverClass(jdbcDriver);
    dataSource.setUsername(username);
    dataSource.setPassword(password);

    return dataSource;
  }

  @Override
  public void init() {
    
  }

}
