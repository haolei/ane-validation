/*
 * Copyright (c) 2013 Active Network Inc. All rights reserved.
 */
package com.active.validation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Null;


/**
 * @author hhao
 *
 */
public class NullValidator implements ConstraintValidator<Null, Object> {

  public void initialize( Null parameters ) {}

  public boolean isValid( Object object, ConstraintValidatorContext constraintValidatorContext ) {
    return object == null;
  }
}
