package com.github.hitman.spring.config.datasource;

import javax.sql.DataSource;

public interface DataSourceCreator<DS extends DataSource> {

  DS dataSource();

}