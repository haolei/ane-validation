/*
 * Copyright (c) 2013 Active Network Inc. All rights reserved.
 */
package com.active.validation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;


/**
 * @author hhao
 *
 */
public class NotNullValidator implements ConstraintValidator<NotNull, Object> {

  public void initialize( NotNull parameters ) {}

  public boolean isValid( Object object, ConstraintValidatorContext constraintValidatorContext ) {
    return object != null;
  }
}
