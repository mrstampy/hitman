package com.github.mrstampy.hit.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Optional convenience superclass for Hibernate <a
 * href="http://docs.jboss.org/hibernate/orm/4.2/manual/en-US/html/ch05.html"
 * >multi-part entity keys</a>, overriding {@link #toString()} for convenience
 * and enforcing the implementation of {@link #hashCode()} and
 * {@link #equals(Object)}.<br>
 * <br>
 * Not for use with single column keys.
 * 
 * @author burton
 * 
 */
public abstract class AbstractMultipartKey implements Serializable {
  private static final long serialVersionUID = 2054496103846610939L;

  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  /**
   * <a href=
   * "http://commons.apache.org/proper/commons-lang/javadocs/api-3.1/org/apache/commons/lang3/builder/EqualsBuilder.html"
   * >EqualsBuilder</a> can assist with implementation.
   */
  public abstract boolean equals(Object o);

  /**
   * <a href=
   * "http://commons.apache.org/proper/commons-lang/javadocs/api-3.1/org/apache/commons/lang3/builder/HashCodeBuilder.html"
   * >HashCodeBuilder</a> can assist with implementation.
   */
  public abstract int hashCode();

}
