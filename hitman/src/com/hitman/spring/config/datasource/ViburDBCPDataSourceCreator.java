package com.hitman.spring.config.datasource;

import java.sql.Connection;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.vibur.dbcp.ViburDBCPDataSource;
import org.vibur.dbcp.listener.DestroyListener;
import org.vibur.dbcp.pool.PoolOperations;

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

}
