/*
 * Copyright (c) 2013 Active Network Inc. All rights reserved.
 */
package com.active.validation;

import javax.validation.Configuration;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;
import javax.validation.Validator;
import javax.validation.ValidatorContext;


/**
 * @author hhao
 *
 */
public class ValidatorContextImpl implements ValidatorContext {

  private Configuration<?> configuraction;
  private ValidatorFactoryImpl validatorFactory;

  public ValidatorContextImpl( Configuration<?> configuraction, ValidatorFactoryImpl validatorFactory ) {
    this.configuraction = configuraction;
    this.validatorFactory = validatorFactory;
  }

  /* (non-Javadoc)
   * @see javax.validation.ValidatorContext#messageInterpolator(javax.validation.MessageInterpolator)
   */
  public ValidatorContext messageInterpolator( MessageInterpolator messageInterpolator ) {
    configuraction.messageInterpolator( messageInterpolator );
    return this;
  }

  /* (non-Javadoc)
   * @see javax.validation.ValidatorContext#traversableResolver(javax.validation.TraversableResolver)
   */
  public ValidatorContext traversableResolver( TraversableResolver traversableResolver ) {
    configuraction.traversableResolver( traversableResolver );
    return this;
  }

  /* (non-Javadoc)
   * @see javax.validation.ValidatorContext#constraintValidatorFactory(javax.validation.ConstraintValidatorFactory)
   */
  public ValidatorContext constraintValidatorFactory( ConstraintValidatorFactory factory ) {
    configuraction.constraintValidatorFactory( factory );
    return this;
  }

  /* (non-Javadoc)
   * @see javax.validation.ValidatorContext#parameterNameProvider(javax.validation.ParameterNameProvider)
   */
  public ValidatorContext parameterNameProvider( ParameterNameProvider parameterNameProvider ) {
    configuraction.parameterNameProvider( parameterNameProvider );
    return this;
  }

  /* (non-Javadoc)
   * @see javax.validation.ValidatorContext#getValidator()
   */
  public Validator getValidator() {
    return new ValidatorImpl( validatorFactory );
  }

}
