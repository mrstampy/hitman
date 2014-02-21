package com.github.mrstampy.hit.example;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.github.mrstampy.hit.entity.AbstractEntity;

@Entity
@Table(name = "hit", schema = "hit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Hit extends AbstractEntity {
  private static final long serialVersionUID = -5867494274708524421L;
  
  private int id;
  private String value;
  private boolean awesome;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public boolean isAwesome() {
    return awesome;
  }

  public void setAwesome(boolean awesome) {
    this.awesome = awesome;
  }

}
