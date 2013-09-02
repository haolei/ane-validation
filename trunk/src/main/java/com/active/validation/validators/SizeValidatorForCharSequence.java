package com.active.validation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Size;

public class SizeValidatorForCharSequence implements ConstraintValidator<Size, CharSequence> {

  private int min;
  private int max;

  public void initialize( Size parameters ) {
    min = parameters.min();
    max = parameters.max();
  }

  public boolean isValid( CharSequence value, ConstraintValidatorContext context ) {
    if ( value == null ) { return true; }
    int length = value.length();
    return length >= min && length <= max;
  }

}
