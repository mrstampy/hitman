package com.github.hitman.utils.evictor;

import com.github.hitman.entity.AbstractEntity;

public interface CacheEvictor {
  
  void evictAllExcept(String...excludedRegions);

  void evictCache(String cacheRegion);

  void evictEntityCache(Class<? extends AbstractEntity> clazz);

  void evictEntityCaches();

  void evictQueryCaches();

  void evictQueryCache(String cacheRegion);

}