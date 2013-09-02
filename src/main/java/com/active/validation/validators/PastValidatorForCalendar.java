package com.active.validation.validators;

import java.util.Calendar;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Past;


public class PastValidatorForCalendar implements ConstraintValidator<Past, Calendar> {

  public void initialize(Past constraintAnnotation) {
  }

  public boolean isValid(Calendar cal, ConstraintValidatorContext constraintValidatorContext) {
      //null values are valid
      if ( cal == null ) {
          return true;
      }
      return cal.before( Calendar.getInstance() );
  }
}
