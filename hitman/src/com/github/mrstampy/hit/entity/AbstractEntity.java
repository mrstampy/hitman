package com.github.mrstampy.hit.entity;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Optional convenience superclass for Hibernate <a
 * href="http://docs.jboss.org/hibernate/orm/4.2/manual/en-US/html/ch05.html"
 * >entities</a>, overriding {@link #toString()} in order to facilitate logging
 * entities and debugging the use of entities.
 * 
 * @author burton
 * 
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
  private static final long serialVersionUID = 2054496103846610939L;

  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
