package com.github.hitman.spring.config.datasource;

import java.util.Properties;

import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;

public class BoneCPDataSourceCreator extends AbstractDataSourceCreator<LazyConnectionDataSourceProxy> {

  @Override
  protected LazyConnectionDataSourceProxy createDataSource(Properties props) throws Exception {
    return new LazyConnectionDataSourceProxy(createBoneCPDataSource(props));
  }

  private BoneCPDataSource createBoneCPDataSource(Properties props) throws Exception {
    BoneCPDataSource dataSource = null;
    
    if(props == null) {
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

}
