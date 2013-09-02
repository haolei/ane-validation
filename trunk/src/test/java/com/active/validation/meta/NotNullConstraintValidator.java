/*
 * Copyright (c) 2013 Active Network Inc. All rights reserved.
 */
package com.active.validation.meta;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;


/**
 * @author hhao
 *
 */
public class NotNullConstraintValidator implements ConstraintValidator<NotNull, String> {

  /* (non-Javadoc)
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  public void initialize( NotNull constraintAnnotation ) {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
   */
  public boolean isValid( String value, ConstraintValidatorContext context ) {
    // TODO Auto-generated method stub
    return false;
  }

}
