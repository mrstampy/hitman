package com.github.mrstampy.hit.jmx;

import net.sf.ehcache.CacheManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import com.github.mrstampy.hit.utils.evictor.CacheEvictor;

/**
 * Provides the ability to evict second level cache via JMX
 * 
 * @author burton
 * 
 */
@Component
@ManagedResource(description = "JMX Second Level Cache Eviction", objectName = "com.github.hitman.jmx:name=JmxCacheEvictor")
public class JmxCacheEvictor {
  private static final Logger log = LoggerFactory.getLogger(JmxCacheEvictor.class);

  @Autowired
  private CacheEvictor cacheEvictor;

  @ManagedOperation(description = "Evicts the specified cache region")
  @ManagedOperationParameters({ @ManagedOperationParameter(name = "cacheRegion", description = "The name of the cache region to evict") })
  public void evict(String cacheRegion) {
    log.debug("JMX eviction of cache region '{}'", cacheRegion);
    cacheEvictor.evictCache(cacheRegion);
  }

  @ManagedAttribute(description = "Returns the names of the second level cache regions")
  public String[] getCacheRegions() {
    log.debug("JMX returning all cache region names");
    return CacheManager.getInstance().getCacheNames();
  }

  @ManagedOperation(description = "Evicts all cache region")
  public void evictAll() {
    log.debug("JMX evicting all cache regions");
    cacheEvictor.evictAllExcept();
  }
}
