/*
 * Copyright (c) 2013 Active Network Inc. All rights reserved.
 */
package com.active.validation.validators;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Min;


/**
 * @author hhao
 *
 */
public class MinValidatorForNumber implements ConstraintValidator<Min, Number> {

  private long minValue;

  public void initialize( Min minValue ) {
    this.minValue = minValue.value();
  }

  public boolean isValid( Number value, ConstraintValidatorContext constraintValidatorContext ) {
    //null values are valid
    if ( value == null ) { return true; }
    if ( value instanceof BigDecimal ) {
      return ( (BigDecimal)value ).compareTo( BigDecimal.valueOf( minValue ) ) != -1;
    }
    else if ( value instanceof BigInteger ) {
      return ( (BigInteger)value ).compareTo( BigInteger.valueOf( minValue ) ) != -1;
    }
    else {
      long longValue = value.longValue();
      return longValue >= minValue;
    }
  }
}
