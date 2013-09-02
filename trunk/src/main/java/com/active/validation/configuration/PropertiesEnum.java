/*
 * Copyright (c) 2013 Active Network Inc. All rights reserved.
 */
package com.active.validation.configuration;

import java.util.Map;


/**
 * @author hhao
 *
 */
public enum PropertiesEnum {
  INTERPOLATE_MESSAGE("InterpolateMessage", "True");

  private String key;
  private String value;

  private PropertiesEnum( String key, String value ) {
    this.key = key;
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public String getKey() {
    return key;
  }

  public static void addAll( Map<String, String> propertiesMap ) {
    for ( PropertiesEnum property : PropertiesEnum.values() ) {
      propertiesMap.put( property.getKey(), property.getValue() );
    }
  }

}
