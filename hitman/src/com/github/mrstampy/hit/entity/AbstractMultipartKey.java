package com.github.mrstampy.hit.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class AbstractMultipartKey implements Serializable {
  private static final long serialVersionUID = 2054496103846610939L;

  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
  
  public abstract boolean equals(Object o);
  
  public abstract int hashCode();

}
