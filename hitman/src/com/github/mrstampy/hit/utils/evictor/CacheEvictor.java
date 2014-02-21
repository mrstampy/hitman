package com.github.mrstampy.hit.utils.evictor;

import com.github.mrstampy.hit.entity.AbstractEntity;

public interface CacheEvictor {
  
  void evictAllExcept(String...excludedRegions);

  void evictCache(String cacheRegion);

  void evictEntityCache(Class<? extends AbstractEntity> clazz);

  void evictEntityCaches();

  void evictQueryCaches();

  void evictQueryCache(String cacheRegion);

}