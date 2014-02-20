package com.github.hitman.dao;

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

public abstract class AbstractDao<ENTITY extends Serializable, KEY extends Serializable> implements Serializable {
  private static final long serialVersionUID = -2523574623457952218L;

  @Autowired
  protected SessionFactory sessionFactory;

  @SuppressWarnings("unchecked")
  @Transactional(readOnly = true)
  public List<ENTITY> getAll() {
    Criteria c = createEntityCriteria();

    return c.list();
  }

  @Transactional(readOnly = true)
  public ENTITY byId(KEY key) {
    return byId(getEntityClass(), key);
  }

  @SuppressWarnings("unchecked")
  @Transactional
  public ENTITY persist(ENTITY entity) {
    if (entity == null) {
      return null;
    }

    return (ENTITY) getSession().merge(entity);
  }

  @Transactional
  public void delete(ENTITY entity) {
    if (entity == null) {
      return;
    }

    getSession().delete(entity);
  }

  @Transactional
  public void deleteKey(KEY key) {
    delete(byId(key));
  }

  @Transactional(readOnly = true)
  public boolean exists(KEY key) {
    return exists(getEntityClass(), key);
  }

  public abstract Class<ENTITY> getEntityClass();

  @SuppressWarnings("unchecked")
  protected <T extends Serializable, K extends Serializable> T byId(Class<T> clazz, K key) {
    return (T) getSession().byId(clazz).getReference(key);
  }

  protected <T extends Serializable, K extends Serializable> boolean exists(Class<T> clazz, K key) {
    return getSession().byId(clazz).load(key) != null;
  }

  protected Query createQuery(String queryString) {
    return getSession().createQuery(queryString);
  }

  protected Query createCacheableQuery(String queryString) {
    Query q = createQuery(queryString);

    setCacheable(q);

    return q;
  }

  protected SQLQuery createSQLQuery(String queryString) {
    return getSession().createSQLQuery(queryString);
  }

  protected SQLQuery createCacheableSQLQuery(String queryString) {
    SQLQuery q = createSQLQuery(queryString);

    setCacheable(q);

    return q;
  }

  protected Session getSession() {
    Session session = sessionFactory.getCurrentSession();

    return session;
  }

  protected Criteria createEntityCriteria() {
    return createCriteria(getEntityClass());
  }

  protected <T extends Serializable> Criteria createCriteria(Class<T> clazz) {
    return getSession().createCriteria(clazz);
  }

  protected <T extends Serializable> Criteria createCacheableCriteria(Class<T> clazz) {
    Criteria c = createCriteria(clazz);

    setCacheable(c);

    return c;
  }

  protected Criteria createCacheableEntityCriteria() {
    Criteria c = createEntityCriteria();

    setCacheable(c);

    return c;
  }

  protected void setCacheable(Criteria c) {
    c.setCacheable(true);
    c.setCacheMode(CacheMode.NORMAL);
  }

  protected void setCacheable(Query q) {
    q.setCacheable(true);
    q.setCacheMode(CacheMode.NORMAL);
  }

}
