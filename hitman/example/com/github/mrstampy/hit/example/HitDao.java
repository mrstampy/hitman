package com.github.mrstampy.hit.example;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.github.mrstampy.hit.dao.AbstractDao;

@Repository // NOTE: the important annotation
public class HitDao extends AbstractDao<Hit, Integer> {

  private static final long serialVersionUID = -3307971782492686725L;

  @Override
  public Class<Hit> getEntityClass() {
    return Hit.class;
  }
  
  @SuppressWarnings("unchecked")
  @Transactional
  public List<Hit> getAllCacheable() {
    Criteria c = createCacheableEntityCriteria();
    
    return c.list();
  }

}
