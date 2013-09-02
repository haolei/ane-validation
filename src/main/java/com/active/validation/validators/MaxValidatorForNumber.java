/*
 * Copyright (c) 2013 Active Network Inc. All rights reserved.
 */
package com.active.validation.validators;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Max;


/**
 * @author hhao
 *
 */
public class MaxValidatorForNumber implements ConstraintValidator<Max, Number> {

  private long maxValue;

  public void initialize( Max maxValue ) {
    this.maxValue = maxValue.value();
  }

  public boolean isValid( Number value, ConstraintValidatorContext constraintValidatorContext ) {
    //null values are valid
    if ( value == null ) { return true; }
    if ( value instanceof BigDecimal ) {
      return ( (BigDecimal)value ).compareTo( BigDecimal.valueOf( maxValue ) ) != 1;
    }
    else if ( value instanceof BigInteger ) {
      return ( (BigInteger)value ).compareTo( BigInteger.valueOf( maxValue ) ) != 1;
    }
    else {
      long longValue = value.longValue();
      return longValue <= maxValue;
    }
  }
}
