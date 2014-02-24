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

/**
 * HIT example initializer and tester.
 * 
 * @author burton
 * 
 */
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

  /**
   * Executes a series of operations on the 'hit' database. Three {@link Hit}
   * records are inserted, then the list is queried four times demonstrating the
   * effectiveness of second level caching (three times in separate threads).
   * The records are then deleted.
   * 
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    HitInitializer hitter = new HitInitializer();

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
    log.info("******** This get all will get from second level cache, will be ~ 10X faster");
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
