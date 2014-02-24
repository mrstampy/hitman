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

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.github.mrstampy.hit.dao.AbstractDao;

/**
 * HIT example data access object implementation.
 * 
 * @author burton
 * 
 */
@Repository
public class HitDao extends AbstractDao<Hit, Integer> {

  private static final long serialVersionUID = -3307971782492686725L;

  @Override
  public Class<Hit> getEntityClass() {
    return Hit.class;
  }

  /**
   * Returns all {@link Hit} records in the database, caching the query and
   * results in second level cache.
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  @Transactional
  public List<Hit> getAllCacheable() {
    Criteria c = createCacheableEntityCriteria();

    return c.list();
  }

}
