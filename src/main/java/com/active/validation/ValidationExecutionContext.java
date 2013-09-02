/*
 * Copyright (c) 2013 Active Network Inc. All rights reserved.
 */
package com.active.validation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator;
import javax.validation.metadata.ConstraintDescriptor;


/**
 * @author hhao
 *
 */
public class ValidationExecutionContext<T> {

  private MessageInterpolator messageInterpolator;

  public ValidationExecutionContext( MessageInterpolator messageInterpolator ) {
    this.messageInterpolator = messageInterpolator;
  }

  boolean shouldStopValidation = false;

  List<ConstraintViolation<T>> failViolation = new ArrayList<ConstraintViolation<T>>();

  public void addToExecution( ConstraintValidatorContext validationContext, T object, Class<T> clazz, Object value, String messageTemplate,
      ConstraintDescriptor<?> constrainDescriptor,
      boolean isValid ) {

    if ( !isValid ) {
      ConstraintViolation<T> c = new ConstraintViolationImpl<T>( messageInterpolator, validationContext, object, clazz, value, messageTemplate, constrainDescriptor );
      failViolation.add( c );
    }

  }

  public Set<ConstraintViolation<T>> getAllConstraintVoilation() {
    return new HashSet<ConstraintViolation<T>>( failViolation );
  }

  public boolean shouldStop() {
    if ( failViolation.isEmpty() ) { return false; }

    ConstraintViolation<T> c = failViolation.get( failViolation.size() - 1 );

    if ( c.getConstraintDescriptor().isReportAsSingleViolation() ) { return true; }

    return false;
  }

  public void SetStop() {
    shouldStopValidation = true;
  }

}
