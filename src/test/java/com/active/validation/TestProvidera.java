/*
 * Copyright (c) 2013 Active Network Inc. All rights reserved.
 */
package com.active.validation;

import javax.validation.Configuration;
import javax.validation.ValidatorFactory;
import javax.validation.spi.BootstrapState;
import javax.validation.spi.ConfigurationState;
import javax.validation.spi.ValidationProvider;

import com.active.validation.configuration.SimpleBeanConfiguration;


/**
 * @author hhao
 *
 */
public class TestProvidera implements ValidationProvider<SimpleBeanConfiguration> {

  /* (non-Javadoc)
   * @see javax.validation.spi.ValidationProvider#createSpecializedConfiguration(javax.validation.spi.BootstrapState)
   */
  public SimpleBeanConfiguration createSpecializedConfiguration( BootstrapState state ) {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see javax.validation.spi.ValidationProvider#createGenericConfiguration(javax.validation.spi.BootstrapState)
   */
  public Configuration<?> createGenericConfiguration( BootstrapState state ) {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see javax.validation.spi.ValidationProvider#buildValidatorFactory(javax.validation.spi.ConfigurationState)
   */
  public ValidatorFactory buildValidatorFactory( ConfigurationState configurationState ) {
    // TODO Auto-generated method stub
    return null;
  }
}
