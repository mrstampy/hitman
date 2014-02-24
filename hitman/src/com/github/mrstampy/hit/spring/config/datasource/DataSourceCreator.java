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

import javax.sql.DataSource;

/**
 * Implementations return a DataSource implementation. For simple, low to medium
 * volume data access typically 6 properties are of importance when configuring
 * a datasource: <br>
 * <br>
 * <ol>
 * <li>
 * The JDBC driver class</li>
 * <li>
 * The JDBC URL to the database</li>
 * <li>
 * The username for the database</li>
 * <li>
 * The password for the database</li>
 * <li>
 * The maximum number of connections the datasource can pool</li>
 * <li>
 * The minimum number of connections in the pool when idle</li>
 * </ol>
 * <br>
 * <br>
 * As each datasource implementation varies in its configuration API, the value
 * of these properties are set via the values in 'hibernate.properties' (the
 * first four) and 'hit.properties' (the last two). Should greater configuration
 * control be required a datasource-specific properties file can be made
 * available on the classpath and specified by the 'datasource.property.file'
 * property in 'hit.properties', with the exception of the first four properties
 * which are always set from 'hibernate.properties' values.
 * 
 * @author burton
 * 
 * @param <DS>
 */
public interface DataSourceCreator<DS extends DataSource> {

  DS dataSource();

}