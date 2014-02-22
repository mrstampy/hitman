package com.github.mrstampy.hit.utils.evictor;

import java.io.Serializable;

/**
 * This interface defines the methods to evict second level cache regions via
 * the session factory.
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