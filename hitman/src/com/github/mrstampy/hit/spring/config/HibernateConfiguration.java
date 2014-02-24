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
package com.github.mrstampy.hit.spring.config;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.github.mrstampy.hit.spring.config.datasource.AbstractDataSourceCreator;
import com.github.mrstampy.hit.spring.config.datasource.BoneCPDataSourceCreator;
import com.github.mrstampy.hit.spring.config.datasource.C3P0DataSourceCreator;
import com.github.mrstampy.hit.spring.config.datasource.DataSourceCreator;
import com.github.mrstampy.hit.spring.config.datasource.TomcatJDBCDataSourceCreator;
import com.github.mrstampy.hit.spring.config.datasource.ViburDBCPDataSourceCreator;
import com.github.mrstampy.hit.utils.evictor.CacheEvictor;
import com.github.mrstampy.hit.utils.evictor.EhCacheEvictor;

/**
 * Spring/Hibernate configuration class, enabling <a href=
 * "http://docs.spring.io/spring/docs/4.0.2.RELEASE/spring-framework-reference/html/aop.html"
 * >AspectJ proxying</a> and <a href=
 * "http://docs.spring.io/spring/docs/4.0.2.RELEASE/spring-framework-reference/html/transaction.html"
 * >transaction management</a>, scanning the project for Spring beans to load.
 * 
 * @author burton
 * 
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
@ComponentScan("com.github.mrstampy.hit")
public class HibernateConfiguration {
  private static final Logger log = LoggerFactory.getLogger(HibernateConfiguration.class);

  /**
   * Corresponds to the value of the 'data.source.creator.type' property.
   * 
   * @author burton
   * 
   */
  public enum DataSourceCreatorType {
    TOMCAT, C3P0, BONECP, VIBUR;
  }

  @Value("${entity.packages}")
  private String[] entityPackages;

  @Value("${data.source.creator.type}")
  private DataSourceCreatorType creatorType;

  @Autowired
  private DataSourceCreator<?> dataSourceCreator;

  @Autowired
  private SessionFactory sessionFactory;

  /**
   * Returns the {@link AbstractDataSourceCreator} subclass specified by the
   * value of the property 'data.source.creator.type'.
   * 
   * @return
   * @see AbstractDataSourceCreator
   */
  @Bean
  public DataSourceCreator<?> dataSourceCreator() {
    log.debug("Creating dataSourceCreator {}", creatorType);
    switch (creatorType) {
    case BONECP:
      return new BoneCPDataSourceCreator();
    case C3P0:
      return new C3P0DataSourceCreator();
    case TOMCAT:
      return new TomcatJDBCDataSourceCreator();
    case VIBUR:
      return new ViburDBCPDataSourceCreator();
    default:
      throw new IllegalArgumentException("data.source.creator.type must be one of BONECP, C3P0, VIBUR, or TOMCAT");
    }
  }

  /**
   * Returns the Hibernate session factory, configured with the values in
   * 'hibernate.properties' and scans the packages specified by the value of the
   * property 'entity.packages' for <a href=
   * "http://docs.jboss.org/hibernate/orm/4.2/manual/en-US/html/ch05.html"
   * >Hibernate entity classes</a>.
   * 
   * @return
   */
  @Bean
  public SessionFactory sessionFactory() {
    log.debug("Creating session factory");
    LocalSessionFactoryBuilder builder = new LocalSessionFactoryBuilder(dataSourceCreator.dataSource());

    builder.scanPackages(entityPackages);

    return builder.buildSessionFactory();
  }

  /**
   * Returns the Hibernate transaction manager.
   * 
   * @return
   */
  @Bean
  public PlatformTransactionManager transactionManager() {
    log.debug("Creating transaction manager");
    return new HibernateTransactionManager(sessionFactory);
  }

  /**
   * Returns the {@link CacheEvictor}.
   * 
   * @return
   */
  @Bean
  public CacheEvictor cacheEvictor() {
    log.debug("Creating cache evictor");
    return new EhCacheEvictor();
  }
}
