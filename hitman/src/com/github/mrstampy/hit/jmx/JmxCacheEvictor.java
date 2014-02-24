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

import com.github.mrstampy.hit.spring.config.JmxConfiguration;
import com.github.mrstampy.hit.utils.evictor.CacheEvictor;

/**
 * Provides the ability to evict second level cache via JMX.
 * 
 * @author burton
 * @see CacheEvictor
 * @see JmxConfiguration
 */
@Component
@ManagedResource(description = "JMX Second Level Cache Eviction", objectName = "com.github.mrstampy.hit.jmx:name=JmxCacheEvictor")
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
