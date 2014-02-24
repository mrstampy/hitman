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
package com.github.mrstampy.hit.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class encapsulates the Hibernate <a
 * href="http://docs.jboss.org/hibernate/orm/4.2/manual/en-US/html/ch03.html"
 * >session factory</a> and via Spring's <a href=
 * "http://docs.spring.io/spring/docs/4.0.2.RELEASE/spring-framework-reference/html/transaction.html"
 * >transaction management</a> provides transactional boundaries around data
 * operations. Calling {@link #getSession()} will return the current session
 * started by a transaction.<br>
 * <br>
 * Methods exist in this class to obtain the Hibernate <a href=
 * "http://docs.jboss.org/hibernate/orm/4.2/manual/en-US/html/ch11.html#objectstate-querying"
 * >query objects</a>. Methods are also available providing the ability to <a
 * href=
 * "http://docs.jboss.org/hibernate/orm/4.2/manual/en-US/html/ch20.html#performance-querycache"
 * >cache queries</a> should <a href=
 * "http://docs.jboss.org/hibernate/orm/4.2/manual/en-US/html/ch20.html#performance-cache"
 * >second level caching</a> be enabled.<br>
 * <br>
 * Subclass to create a Hibernate entity-specific data access object. For more
 * information refer to the <a
 * href="http://docs.jboss.org/hibernate/orm/4.2/manual/en-US/html/ch05.html"
 * >Hibernate</a> documentation.
 * 
 * @author burton
 * 
 * @param <ENTITY>
 *          the serializable Hibernate entity class
 * @param <KEY>
 *          the serializable key class for the entity
 */
public abstract class AbstractDao<ENTITY extends Serializable, KEY extends Serializable> implements Serializable {
  private static final long serialVersionUID = -2523574623457952218L;

  /**
   * The session factory is available to all subclasses, but should be used only
   * if one must.
   */
  @Autowired
  protected SessionFactory sessionFactory;

  /**
   * Returns the full list of entities. Use sparingly if retrieving large
   * datasets, and if in doubt a method in the subclass limiting rows returned
   * is recommended.
   * 
   * @return the full list of entities
   */
  @SuppressWarnings("unchecked")
  @Transactional
  public List<ENTITY> getAll() {
    Criteria c = createEntityCriteria();

    return c.list();
  }

  /**
   * Return the entity specified by the key.
   * 
   * @param key
   * @return
   */
  @Transactional
  public ENTITY byId(KEY key) {
    return byId(getEntityClass(), key);
  }

  /**
   * Persist the given entity to the database.
   * 
   * @param entity
   * @return
   */
  @SuppressWarnings("unchecked")
  @Transactional
  public ENTITY persist(ENTITY entity) {
    if (entity == null) {
      return null;
    }

    return (ENTITY) getSession().merge(entity);
  }

  /**
   * Delete the given entity from the database
   * 
   * @param entity
   */
  @Transactional
  public void delete(ENTITY entity) {
    if (entity == null) {
      return;
    }

    getSession().delete(entity);
  }

  /**
   * Deletes the entity specified by the key from the database
   * 
   * @param key
   */
  @Transactional
  public void deleteKey(KEY key) {
    delete(byId(key));
  }

  /**
   * Returns true should the entity specified by the key exist in the database.
   * 
   * @param key
   * @return
   */
  @Transactional(readOnly = true)
  public boolean exists(KEY key) {
    return exists(getEntityClass(), key);
  }

  /**
   * Implement to return the class object of the entity.
   * 
   * @return
   */
  public abstract Class<ENTITY> getEntityClass();

  /**
   * Returns the database entity specified by its entity class and key. Any
   * entity can be obtained via this method, allowing the DAO subclass to
   * perform multi-table operations in implemented methods.
   * 
   * @param clazz
   * @param key
   * @return
   */
  @SuppressWarnings("unchecked")
  protected <T extends Serializable, K extends Serializable> T byId(Class<T> clazz, K key) {
    return (T) getSession().byId(clazz).getReference(key);
  }

  /**
   * Returns true should the entity specified by it's class and key exist in the
   * database.
   * 
   * @param key
   * @return
   */
  protected <T extends Serializable, K extends Serializable> boolean exists(Class<T> clazz, K key) {
    return getSession().byId(clazz).load(key) != null;
  }

  /**
   * Returns a query object created from an <a href=
   * "http://docs.jboss.org/hibernate/orm/4.2/manual/en-US/html/ch16.html"
   * >HQL</a> string.
   * 
   * @param queryString
   * @return
   */
  protected Query createQuery(String queryString) {
    return getSession().createQuery(queryString);
  }

  /**
   * Returns a query object created from an <a href=
   * "http://docs.jboss.org/hibernate/orm/4.2/manual/en-US/html/ch16.html"
   * >HQL</a> string. The Query returned will be cached in <a href=
   * "http://docs.jboss.org/hibernate/orm/4.2/manual/en-US/html/ch20.html#performance-cache"
   * >second level caching</a>, if enabled.
   * 
   * @param queryString
   * @return
   */
  protected Query createCacheableQuery(String queryString) {
    Query q = createQuery(queryString);

    setCacheable(q);

    return q;
  }

  /**
   * Returns an sql query object created from an <a href=
   * "http://docs.jboss.org/hibernate/orm/4.2/manual/en-US/html/ch18.html"
   * >SQL</a> string.
   * 
   * @param queryString
   * @return
   */
  protected SQLQuery createSQLQuery(String queryString) {
    return getSession().createSQLQuery(queryString);
  }

  /**
   * Returns an sql query object created from an <a href=
   * "http://docs.jboss.org/hibernate/orm/4.2/manual/en-US/html/ch18.html"
   * >SQL</a> string. The SQLQuery returned will be cached in <a href=
   * "http://docs.jboss.org/hibernate/orm/4.2/manual/en-US/html/ch20.html#performance-cache"
   * >second level caching</a>, if enabled.
   * 
   * @param queryString
   * @return
   */
  protected SQLQuery createCacheableSQLQuery(String queryString) {
    SQLQuery q = createSQLQuery(queryString);

    setCacheable(q);

    return q;
  }

  /**
   * Returns the current session as created by Spring's <a href=
   * "http://docs.spring.io/spring/docs/4.0.2.RELEASE/spring-framework-reference/html/transaction.html"
   * >transaction management</a>.
   * 
   * @return
   */
  protected Session getSession() {
    Session session = sessionFactory.getCurrentSession();

    return session;
  }

  /**
   * Returns a <a href=
   * "http://docs.jboss.org/hibernate/orm/4.2/manual/en-US/html/ch17.html"
   * >criteria query object</a> for the entity specified in the subclass.
   * 
   * @param queryString
   * @return
   * @see #getEntityClass()
   */
  protected Criteria createEntityCriteria() {
    return createCriteria(getEntityClass());
  }

  /**
   * Returns a <a href=
   * "http://docs.jboss.org/hibernate/orm/4.2/manual/en-US/html/ch17.html"
   * >criteria query object</a> for the entity specified.
   * 
   * @param queryString
   * @return
   * @see #getEntityClass()
   */
  protected <T extends Serializable> Criteria createCriteria(Class<T> clazz) {
    return getSession().createCriteria(clazz);
  }

  /**
   * Returns a <a href=
   * "http://docs.jboss.org/hibernate/orm/4.2/manual/en-US/html/ch17.html"
   * >criteria query object</a> for the entity specified. The Criteria returned
   * will be cached in <a href=
   * "http://docs.jboss.org/hibernate/orm/4.2/manual/en-US/html/ch20.html#performance-cache"
   * >second level caching</a>, if enabled.
   * 
   * @param queryString
   * @return
   * @see #getEntityClass()
   */
  protected <T extends Serializable> Criteria createCacheableCriteria(Class<T> clazz) {
    Criteria c = createCriteria(clazz);

    setCacheable(c);

    return c;
  }

  /**
   * Returns a <a href=
   * "http://docs.jboss.org/hibernate/orm/4.2/manual/en-US/html/ch17.html"
   * >criteria query object</a> for the entity specified in the subclass. The
   * Criteria returned will be cached in <a href=
   * "http://docs.jboss.org/hibernate/orm/4.2/manual/en-US/html/ch20.html#performance-cache"
   * >second level caching</a>, if enabled.
   * 
   * @param queryString
   * @return
   * @see #getEntityClass()
   */
  protected Criteria createCacheableEntityCriteria() {
    Criteria c = createEntityCriteria();

    setCacheable(c);

    return c;
  }

  /**
   * Sets the specified criteria cacheable with NORMAL cache mode.
   * 
   * @param c
   */
  protected void setCacheable(Criteria c) {
    c.setCacheable(true);
    c.setCacheMode(CacheMode.NORMAL);
  }

  /**
   * Sets the specified query cacheable with NORMAL cache mode.
   * 
   * @param c
   */
  protected void setCacheable(Query q) {
    q.setCacheable(true);
    q.setCacheMode(CacheMode.NORMAL);
  }

}
