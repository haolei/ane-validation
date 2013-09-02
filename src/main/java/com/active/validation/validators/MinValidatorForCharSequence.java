/*
 * Copyright (c) 2013 Active Network Inc. All rights reserved.
 */
package com.active.validation.validators;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Min;


/**
 * @author hhao
 *
 */
public class MinValidatorForCharSequence implements ConstraintValidator<Min, CharSequence> {

  private BigDecimal minValue;

  public void initialize( Min minValue ) {
    this.minValue = BigDecimal.valueOf( minValue.value() );
  }

  public boolean isValid( CharSequence value, ConstraintValidatorContext constraintValidatorContext ) {
    //null values are valid
    if ( value == null ) { return true; }
    try {
      return new BigDecimal( value.toString() ).compareTo( minValue ) != -1;
    }
    catch ( NumberFormatException nfe ) {
      return false;
    }
  }
}