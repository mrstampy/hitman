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
