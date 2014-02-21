package com.github.mrstampy.hit.spring.config.datasource;

import javax.sql.DataSource;

public interface DataSourceCreator<DS extends DataSource> {

  DS dataSource();

}