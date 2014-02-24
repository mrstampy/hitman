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
package com.github.mrstampy.hit.spring.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * This aspect wraps all methods marked with a <a href=
 * "http://docs.spring.io/spring/docs/4.0.2.RELEASE/spring-framework-reference/html/transaction.html"
 * >transaction boundary</a> and if the log level is set to debug, will show the
 * time spent executing the method in nanoseconds.
 * 
 * @author burton
 * 
 */
@Aspect
@Component
public class TransactionalProfileAspect {
  private static final Logger log = LoggerFactory.getLogger(TransactionalProfileAspect.class);

  @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
  public Object profile(ProceedingJoinPoint call) throws Throwable {
    if (!log.isDebugEnabled()) {
      return call.proceed();
    }

    long start = System.nanoTime();
    long end = -1;
    try {
      log.trace("Entering {}", call);
      return call.proceed();
    } finally {
      log.trace("Exited {}", call);
      end = System.nanoTime();
      log.debug("{} took {} ns", call, (end - start));
    }
  }

}
