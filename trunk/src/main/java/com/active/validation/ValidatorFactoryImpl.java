/*
 * Copyright (c) 2013 Active Network Inc. All rights reserved.
 */
package com.active.validation;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorContext;
import javax.validation.ValidatorFactory;

import com.active.validation.configuration.SimpleBeanConfiguration;


/**
 * @author hhao
 *
 */
public class ValidatorFactoryImpl implements ValidatorFactory {

  private SimpleBeanConfiguration configuraction;
  private ValidatorContextImpl validatorContextImpl;

  private AtomicBoolean shutdown = new AtomicBoolean( false );

  public ValidatorFactoryImpl( SimpleBeanConfiguration configuraction ) {
    this.configuraction = configuraction;
    validatorContextImpl = new ValidatorContextImpl( this.configuraction, this );
  }

  /* (non-Javadoc)
   * @see javax.validation.ValidatorFactory#getValidator()
   */
  public Validator getValidator() {
    checkCloseStatusWithEx();
    return usingContext().getValidator();
  }

  /* (non-Javadoc)
   * @see javax.validation.ValidatorFactory#usingContext()
   */
  public ValidatorContext usingContext() {
    checkCloseStatusWithEx();
    return validatorContextImpl;
  }

  /* (non-Javadoc)
   * @see javax.validation.ValidatorFactory#getMessageInterpolator()
   */
  public MessageInterpolator getMessageInterpolator() {
    checkCloseStatusWithEx();
    return configuraction.getMessageInterpolator();
  }

  /* (non-Javadoc)
   * @see javax.validation.ValidatorFactory#getTraversableResolver()
   */
  public TraversableResolver getTraversableResolver() {
    checkCloseStatusWithEx();
    return configuraction.getTraversableResolver();
  }

  /* (non-Javadoc)
   * @see javax.validation.ValidatorFactory#getConstraintValidatorFactory()
   */
  public ConstraintValidatorFactory getConstraintValidatorFactory() {
    checkCloseStatusWithEx();
    return configuraction.getConstraintValidatorFactory();
  }

  /* (non-Javadoc)
   * @see javax.validation.ValidatorFactory#getParameterNameProvider()
   */
  public ParameterNameProvider getParameterNameProvider() {
    checkCloseStatusWithEx();
    return configuraction.getParameterNameProvider();
  }

  /* (non-Javadoc)
   * @see javax.validation.ValidatorFactory#unwrap(java.lang.Class)
   */
  public <T> T unwrap( Class<T> type ) {
    if ( ValidatorFactoryImpl.class.equals( type ) ) { return type.cast( this ); }
    throw new ValidationException( "TypeNotSupported" );
  }

  /* (non-Javadoc)
   * @see javax.validation.ValidatorFactory#close()
   */
  public void close() {
    shutdown.compareAndSet( false, true );
  }

  public void checkCloseStatusWithEx() {
    if ( shutdown.get() ) { throw new ValidationException( "ValidatorFactoryClosed" ); }
  }

}
