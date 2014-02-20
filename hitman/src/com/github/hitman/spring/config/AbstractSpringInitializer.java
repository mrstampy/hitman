package com.github.hitman.spring.config;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public abstract class AbstractSpringInitializer {
  private static final Logger log = LoggerFactory.getLogger(AbstractSpringInitializer.class);

  private AnnotationConfigApplicationContext applicationContext;

  private Lock lock = new ReentrantLock(true);

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

  protected <T> T bean(Class<T> clazz) {
    return applicationContext().getBean(clazz);
  }

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
