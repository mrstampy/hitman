package com.github.mrstampy.hitman.spring.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
