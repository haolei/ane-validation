package com.active.validation.validators;

import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Past;


public class PastValidatorForDate implements ConstraintValidator<Past, Date> {

  public void initialize( Past constraintAnnotation ) {}

  public boolean isValid( Date date, ConstraintValidatorContext constraintValidatorContext ) {
    //null values are valid
    if ( date == null ) { return true; }
    return date.before( new Date() );
  }

}
