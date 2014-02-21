package com.github.mrstampy.hit.spring.config.datasource;

import javax.sql.DataSource;

/**
 * Implementations return a DataSource implementation. For simple, low to medium
 * volume data access typically 6 properties are of importance when configuring
 * a datasource: <br>
 * <br>
 * <li>
 * <ol>
 * The JDBC driver class
 * </ol>
 * <ol>
 * The JDBC URL to the database
 * </ol>
 * <ol>
 * The username for the database
 * </ol>
 * <ol>
 * The password for the database
 * </ol>
 * <ol>
 * The maximum number of connections the datasource can pool
 * </ol>
 * <ol>
 * The minimum number of connections in the pool when idle
 * </ol>
 * </li><br>
 * <br>
 * As each datasource implementation varies in its configuration API, the value
 * of these properties are set via the values in 'hibernate.properties' (the
 * first four) and 'hit.properties' (the last two). Should greater
 * configuration control be required a datasource-specific properties file can
 * be made available on the classpath and specified by the
 * 'datasource.property.file' property in 'hit.properties' with the exception
 * of the first four properties which are always set from 'hibernate.properties'
 * values.
 * 
 * @author burton
 * 
 * @param <DS>
 */
public interface DataSourceCreator<DS extends DataSource> {

  DS dataSource();

}