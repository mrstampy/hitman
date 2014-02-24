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
