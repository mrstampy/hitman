package com.github.mrstampy.hitman.spring.config.datasource;

import javax.sql.DataSource;

public interface DataSourceCreator<DS extends DataSource> {

  DS dataSource();

}