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
package com.github.mrstampy.hit.utils.evictor;

import java.io.Serializable;

/**
 * This interface defines the methods to evict second level cache regions via
 * the session factory
 * 
 * @author burton
 * 
 */
public interface CacheEvictor {

  /**
   * Evicts all except the specified regions
   * 
   * @param excludedRegions
   */
  void evictAllExcept(String... excludedRegions);

  /**
   * Evicts the specified cache region
   * 
   * @param cacheRegion
   */
  void evictCache(String cacheRegion);

  /**
   * Evicts the specified entity cache region
   * 
   * @param cacheRegion
   */
  void evictEntityCache(Class<? extends Serializable> clazz);

  /**
   * Evicts all entity cache regions
   * 
   */
  void evictEntityCaches();

  /**
   * Evicts all query cache regions
   * 
   */
  void evictQueryCaches();

  /**
   * Evicts the specified query cache region.
   * 
   * @param cacheRegion
   */
  void evictQueryCache(String cacheRegion);

}