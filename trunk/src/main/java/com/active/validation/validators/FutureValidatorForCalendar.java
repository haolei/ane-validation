package com.active.validation.validators;

import java.util.Calendar;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Future;


public class FutureValidatorForCalendar implements ConstraintValidator<Future, Calendar> {

  public void initialize( Future constraintAnnotation ) {}

  public boolean isValid( Calendar cal, ConstraintValidatorContext constraintValidatorContext ) {
    //null values are valid
    if ( cal == null ) { return true; }
    return cal.after( Calendar.getInstance() );
  }
}
