package com.github.mrstampy.hitman.entity;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.ToStringBuilder;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
  private static final long serialVersionUID = 2054496103846610939L;

  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}
