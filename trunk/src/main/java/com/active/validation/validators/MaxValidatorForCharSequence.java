/*
 * Copyright (c) 2013 Active Network Inc. All rights reserved.
 */
package com.active.validation.validators;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Max;


/**
 * @author hhao
 *
 */
public class MaxValidatorForCharSequence implements ConstraintValidator<Max, CharSequence> {

  private BigDecimal maxValue;

  public void initialize( Max maxValue ) {
    this.maxValue = BigDecimal.valueOf( maxValue.value() );
  }

  public boolean isValid( CharSequence value, ConstraintValidatorContext constraintValidatorContext ) {
    //null values are valid
    if ( value == null ) { return true; }
    try {
      return new BigDecimal( value.toString() ).compareTo( maxValue ) != 1;
    }
    catch ( NumberFormatException nfe ) {
      return false;
    }
  }
}
