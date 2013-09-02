package com.active.validation.validators;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Future;


public class FutureValidatorForDate implements ConstraintValidator<Future, Date> {

  public void initialize( Future constraintAnnotation ) {}

  public boolean isValid( Date date, ConstraintValidatorContext constraintValidatorContext ) {
    //null values are valid
    if ( date == null ) { return true; }
    return date.after( new Date() );
  }
}
