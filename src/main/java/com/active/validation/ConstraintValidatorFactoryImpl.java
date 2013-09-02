/*
 * Copyright (c) 2013 Active Network Inc. All rights reserved.
 */
package com.active.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.ValidationException;


/**
 * @author hhao
 *
 */
public class ConstraintValidatorFactoryImpl implements ConstraintValidatorFactory {

  /* (non-Javadoc)
   * @see javax.validation.ConstraintValidatorFactory#getInstance(java.lang.Class)
   */
  public <T extends ConstraintValidator<?, ?>> T getInstance( Class<T> key ) {
    try {
      return (T)key.newInstance();
    }
    catch ( InstantiationException e ) {
      throw new ValidationException( "InstantiationFail", e );

    }
    catch ( IllegalAccessException e ) {
      throw new ValidationException( "InstantiationFail", e );
    }
  }

  /* (non-Javadoc)
   * @see javax.validation.ConstraintValidatorFactory#releaseInstance(javax.validation.ConstraintValidator)
   */
  public void releaseInstance( ConstraintValidator<?, ?> instance ) {

  }

}
