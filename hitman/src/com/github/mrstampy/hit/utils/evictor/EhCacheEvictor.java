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

import java.lang.management.ManagementFactory;

import javax.annotation.PostConstruct;
import javax.management.MBeanServer;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.management.ManagementService;

import org.hibernate.cache.ehcache.internal.regions.EhcacheDataRegion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * EhCache region evictor.
 * 
 * @author burton
 * 
 */
public class EhCacheEvictor extends AbstractCacheEvictor<EhcacheDataRegion> {
  private static final Logger log = LoggerFactory.getLogger(EhCacheEvictor.class);

  @Value("${jmx.enabled}")
  private boolean jmxEnabled;

  /**
   * If enabled, EhCache attributes and actions will be made available via JMX.
   * 
   * @throws Exception
   */
  @PostConstruct
  public void postConstruct() throws Exception {
    if (!jmxEnabled) {
      return;
    }

    CacheManager cm = CacheManager.getInstance();
    MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
    ManagementService.registerMBeans(cm, mBeanServer, true, true, true, true);
  }

  @Override
  protected void evictCacheRegion(EhcacheDataRegion region) {
    log.debug("Evicting EhCache cache '{}'", region.getName());

    Ehcache cache = region.getEhcache();

    cache.removeAll();
  }

}
