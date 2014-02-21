package com.github.mrstampy.hit.utils.evictor;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.io.Serializable;
import java.util.Map.Entry;

import org.hibernate.SessionFactory;
import org.hibernate.cache.spi.Region;
import org.hibernate.engine.spi.CacheImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractCacheEvictor<R extends Region> implements CacheEvictor {
  private static final Logger log = LoggerFactory.getLogger(AbstractCacheEvictor.class);

  @Autowired
  protected SessionFactory sessionFactory;

  @SuppressWarnings("unchecked")
  @Override
  public void evictAllExcept(String... excludedRegions) {
    long start = System.nanoTime();
    long end = -1;

    try {
      for (Entry<String, Region> entry : getCache().getAllSecondLevelCacheRegions().entrySet()) {
        if (isExclusion(entry.getKey(), excludedRegions)) {
          continue;
        }

        evictCacheRegion((R) entry.getValue());
      }
      end = System.nanoTime();
    } finally {
      if (end > start) {
        log.debug("Evict all took {} ns", (end - start));
      }
    }
  }

  private boolean isExclusion(String key, String[] excludedRegions) {
    if (excludedRegions == null || excludedRegions.length == 0) {
      return false;
    }

    for (String ex : excludedRegions) {
      if (ex.equals(key)) {
        return true;
      }
    }

    return false;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void evictCache(String cacheRegion) {
    if (isEmpty(cacheRegion)) {
      log.error("No cache region specified, cannot evict cache");
      return;
    }

    R region = (R) getCache().getAllSecondLevelCacheRegions().get(cacheRegion);
    if (region == null) {
      log.warn("No cache region {}", cacheRegion);
      return;
    }

    evictCacheRegion(region);
  }

  /**
   * Subclasses implement to evict the specified region.
   * 
   * @param region
   */
  protected abstract void evictCacheRegion(R region);

  @Override
  public void evictEntityCache(Class<? extends Serializable> clazz) {
    if (clazz == null) {
      log.error("No class specified to evict entity cache");
      return;
    }

    log.debug("Evicting entity cache '{}'", clazz.getName());
    getCache().evictEntityRegion(clazz);
  }

  @Override
  public void evictEntityCaches() {
    log.debug("Evicting entity cache regions");
    getCache().evictEntityRegions();
  }

  @Override
  public void evictQueryCaches() {
    log.debug("Evicting query regions");
    getCache().evictQueryRegions();
  }

  @Override
  public void evictQueryCache(String cacheRegion) {
    if (isEmpty(cacheRegion)) {
      log.error("No cache region specified, cannot evict cache");
      return;
    }

    log.debug("Evicting query cache region '{}'", cacheRegion);
    getCache().evictQueryRegion(cacheRegion);
  }

  protected CacheImplementor getCache() {
    return (CacheImplementor) sessionFactory.getCache();
  }

}
