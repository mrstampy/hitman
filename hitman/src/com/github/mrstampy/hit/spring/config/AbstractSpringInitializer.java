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

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.github.mrstampy.hit.dao.AbstractDao;

/**
 * Abstract initializer for Spring. The {@link #configure()} implementation will
 * return context specifying all classes annotated with <a href=
 * "http://docs.spring.io/spring/docs/4.0.2.RELEASE/spring-framework-reference/html/beans.html#beans-java-basic-concepts"
 * >the configuration annotation</a>, as required.<br>
 * <br>
 * Implementations can expose the {@link #applicationContext()} and
 * {@link #bean(Class)} methods to allow non-Spring code to obtain Spring bean
 * references ie. to access an {@link AbstractDao} in order to perfom a database
 * operation. For applications implemented in Spring subclasses allow the
 * application to be started in the standard Java runtime environment.
 * 
 * @author burton
 * 
 */
public abstract class AbstractSpringInitializer {
  private static final Logger log = LoggerFactory.getLogger(AbstractSpringInitializer.class);

  private AnnotationConfigApplicationContext applicationContext;

  private Lock lock = new ReentrantLock(true);

  /**
   * Returns the Spring application context, creating one if it has not yet been
   * created.
   * 
   * @return
   * @see #configure()
   */
  protected ApplicationContext applicationContext() {
    if (applicationContext == null) {
      lock.lock();
      try {
        log.info("Initializing context");
        if (applicationContext == null) {
          applicationContext = configure();
        }
        log.info("Context initialized");
      } finally {
        lock.unlock();
      }
    }

    return applicationContext;
  }

  /**
   * Scans packages for Spring annotated classes, if required. This method is
   * provided as convenience and configuration beans annotated with the
   * component scan annotation should be preferred.
   * 
   * @param packages
   */
  protected void scanPackages(String... packages) {
    if (packages == null || packages.length == 0) {
      log.error("No packages specified for scanning; configuration error.  Exiting");
      System.exit(-1);
    }

    applicationContext();

    if (log.isInfoEnabled()) {
      String msg = getLogMessage(packages);
      log.info(msg, (Object[]) packages);
    }

    applicationContext.scan(packages);
  }

  /**
   * Returns the Spring bean specified by the class.
   * 
   * @param clazz
   * @return
   */
  protected <T> T bean(Class<T> clazz) {
    return applicationContext().getBean(clazz);
  }

  /**
   * Stops the application context.
   */
  protected void stop() {
    if (applicationContext == null) {
      log.error("Application context not initialized");
      return;
    }

    log.info("Stopping context");
    applicationContext.stop();
    applicationContext.close();
    log.info("Spring context stopped");
  }

  /**
   * Implement to return a context, specifying all required <a href=
   * "http://docs.spring.io/spring/docs/4.0.2.RELEASE/spring-framework-reference/html/beans.html#beans-java-basic-concepts"
   * >configuration beans</a>. It is expected that any applications will have
   * their own configuration beans in addition to the ones included in HIT.
   * 
   * @return
   * 
   * @see HibernateConfiguration
   * @see JmxConfiguration
   * @see PropertiesConfiguration
   */
  protected abstract AnnotationConfigApplicationContext configure();

  private String getLogMessage(String[] packages) {
    StringBuilder builder = new StringBuilder();

    int num = packages.length;

    builder.append("Adding packages for Spring component scanning: ");
    boolean first = true;
    for (int i = 0; i < num; i++) {
      if (!first) {
        builder.append(", ");
      }
      builder.append("{}");
      first = false;
    }

    return builder.toString();
  }
}
