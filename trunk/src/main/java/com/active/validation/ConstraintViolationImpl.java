/*
 * Copyright (c) 2013 Active Network Inc. All rights reserved.
 */
package com.active.validation;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;


/**
 * @author hhao
 *
 */
public class ConstraintViolationImpl<T> implements ConstraintViolation<T> {
  
  private Class<T> rootBeanClass;

  private T rootBean;
  
  private String messageTemplate;

  private Object invalidValue;

  private ConstraintDescriptor<?> descriptor;
  
  private MessageInterpolator messageInterpolator;

  /**
   * @param validationContext
   * @param clazz
   * @param value
   * @param messageTemplate
   * @param constrainDescriptor
   */
  public ConstraintViolationImpl( MessageInterpolator messageInterpolator, ConstraintValidatorContext validationContext, T object, Class<T> clazz, Object value,
      String messageTemplate,
      ConstraintDescriptor<?> constrainDescriptor ) {
    this.rootBeanClass = clazz;
    this.rootBean = object;
    this.messageTemplate = messageTemplate;
    this.invalidValue = value;
    this.descriptor = constrainDescriptor;
  }

  public String getMessage() {
    return messageInterpolator.interpolate( messageTemplate, new MessageInterpolatorContextImpl( descriptor, invalidValue ) );
  }

  public String getMessageTemplate() {
    return messageTemplate;
  }

  public T getRootBean() {
    return rootBean;
  }

  public Class<T> getRootBeanClass() {
    return rootBeanClass;
  }

  public Object getLeafBean() {
    return null;
  }

  public Object[] getExecutableParameters() {
    return null;
  }

  public Object getExecutableReturnValue() {
    return null;
  }

  public Path getPropertyPath() {
    return null;
  }

  public Object getInvalidValue() {
    return this.invalidValue;
  }

  public ConstraintDescriptor<?> getConstraintDescriptor() {
    return this.descriptor;
  }

  public <U> U unwrap( Class<U> type ) {
    return null;
  }

}
