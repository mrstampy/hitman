package com.github.hitman.spring.config;

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

import com.hitman.spring.config.datasource.BoneCPDataSourceCreator;
import com.hitman.spring.config.datasource.C3P0DataSourceCreator;
import com.hitman.spring.config.datasource.DataSourceCreator;
import com.hitman.spring.config.datasource.TomcatJDBCDataSourceCreator;
import com.hitman.spring.config.datasource.ViburDBCPDataSourceCreator;
import com.hitman.utils.evictor.CacheEvictor;
import com.hitman.utils.evictor.EhCacheEvictor;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableTransactionManagement
@ComponentScan("***REMOVED***")
public class HibernateConfiguration {
  private static final Logger log = LoggerFactory.getLogger(HibernateConfiguration.class);

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

  @Bean
  public SessionFactory sessionFactory() {
    log.debug("Creating session factory");
    LocalSessionFactoryBuilder builder = new LocalSessionFactoryBuilder(dataSourceCreator.dataSource());

    builder.scanPackages(entityPackages);

    return builder.buildSessionFactory();
  }

  @Bean
  public PlatformTransactionManager transactionManager() {
    log.debug("Creating transaction manager");
    return new HibernateTransactionManager(sessionFactory);
  }

  @Bean
  public CacheEvictor cacheEvictor() {
    log.debug("Creating cache evictor");
    return new EhCacheEvictor();
  }
}
