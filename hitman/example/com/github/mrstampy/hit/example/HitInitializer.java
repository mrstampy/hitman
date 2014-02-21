package com.github.mrstampy.hit.example;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.github.mrstampy.hit.spring.config.AbstractSpringInitializer;
import com.github.mrstampy.hit.spring.config.HibernateConfiguration;
import com.github.mrstampy.hit.spring.config.JmxConfiguration;
import com.github.mrstampy.hit.spring.config.PropertiesConfiguration;

public class HitInitializer extends AbstractSpringInitializer {
  private static final Logger log = LoggerFactory.getLogger(HitInitializer.class);

  @Override
  protected AnnotationConfigApplicationContext configure() {
    //@formatter:off
    return new AnnotationConfigApplicationContext(
        PropertiesConfiguration.class, 
        JmxConfiguration.class, 
        HibernateConfiguration.class,
        HitConfiguration.class);
    //@formatter:on
  }

  public static void main(String[] args) throws Exception {
    HitInitializer hitter = new HitInitializer();

    // Use a configuration class with a component scan annotation
    // in preference to this.
    hitter.scanPackages("com.github.mrstampy.hit.example");

    HitDao dao = hitter.bean(HitDao.class);
    
    insert(dao, "Mr Stampy is the");
    insert(dao, "Hibernate Induction Trigger");
    insert(dao, "man");

    log.info("******** This get all will hit the database");
    CountDownLatch cdl = retrieveAll(hitter);
    cdl.await();

    log.info("******** This get all will get from second level cache, will be ~ 10X faster");
    cdl = retrieveAll(hitter);
    cdl.await();

    log.info("******** This get all will get from second level cache, will be ~ 10X faster");
    cdl = retrieveAll(hitter);
    cdl.await();

    delete(dao);

    cdl = retrieveAll(hitter);
    cdl.await();
    
    hitter.stop();
    
    log.info("HIT example complete");
    System.exit(0);
  }

  private static void delete(HitDao dao) {
    List<Hit> list = dao.getAllCacheable();
    for (Hit hit : list) {
      log.info("Deleting hit {}", hit);
      dao.delete(hit);
    }
  }

  private static CountDownLatch retrieveAll(final HitInitializer hitter) {
    final CountDownLatch cdl = new CountDownLatch(1);

    Thread thread = new Thread() {
      public void run() {
        try {
          HitDao dao = hitter.bean(HitDao.class);
          List<Hit> list = dao.getAllCacheable();
          if (list.isEmpty()) {
            log.info("No hits retrieved");
          }
          for (Hit hit : list) {
            log.info("Retrieved hit {}", hit);
          }
        } finally {
          cdl.countDown();
        }
      }
    };

    thread.start();

    return cdl;
  }

  private static void insert(HitDao dao, String value) {
    Hit hit = new Hit();

    hit.setValue(value);
    hit.setAwesome(true);

    Hit persisted = dao.persist(hit);

    log.info("Persisted example {}", persisted);

    int id = persisted.getId();

    log.info("Does the example exist in the database? {}", dao.exists(id));
  }

}
