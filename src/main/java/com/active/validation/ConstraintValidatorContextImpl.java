/*
 * Copyright (c) 2013 Active Network Inc. All rights reserved.
 */
package com.active.validation;

import javax.validation.ConstraintValidatorContext;
import javax.validation.metadata.ConstraintDescriptor;


/**
 * @author hhao
 *
 */
public class ConstraintValidatorContextImpl implements ConstraintValidatorContext {

  private ConstraintDescriptor<?> constraintDescriptor;

  public ConstraintValidatorContextImpl( ConstraintDescriptor<?> constraintDescriptor ) {
    this.constraintDescriptor = constraintDescriptor;
  }


  /* (non-Javadoc)
   * @see javax.validation.ConstraintValidatorContext#getDefaultConstraintMessageTemplate()
   */
  public String getDefaultConstraintMessageTemplate() {
    return (String)constraintDescriptor.getAttributes().get( "message" );
  }

  /* (non-Javadoc)
   * @see javax.validation.ConstraintValidatorContext#buildConstraintViolationWithTemplate(java.lang.String)
   */
  public ConstraintViolationBuilder buildConstraintViolationWithTemplate( String messageTemplate ) {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see javax.validation.ConstraintValidatorContext#unwrap(java.lang.Class)
   */
  public <T> T unwrap( Class<T> type ) {
    // TODO Auto-generated method stub
    return null;
  }
  
  
  class ConstraintViolationBuilderImpl implements ConstraintViolationBuilder {

    /* (non-Javadoc)
     * @see javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder#addNode(java.lang.String)
     */
    public NodeBuilderDefinedContext addNode( String name ) {
      // TODO Auto-generated method stub
      return null;
    }

    /* (non-Javadoc)
     * @see javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder#addPropertyNode(java.lang.String)
     */
    public NodeBuilderCustomizableContext addPropertyNode( String name ) {
      // TODO Auto-generated method stub
      return null;
    }

    /* (non-Javadoc)
     * @see javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder#addBeanNode()
     */
    public LeafNodeBuilderCustomizableContext addBeanNode() {
      // TODO Auto-generated method stub
      return null;
    }

    /* (non-Javadoc)
     * @see javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder#addParameterNode(int)
     */
    public NodeBuilderDefinedContext addParameterNode( int index ) {
      // TODO Auto-generated method stub
      return null;
    }

    /* (non-Javadoc)
     * @see javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder#addConstraintViolation()
     */
    public ConstraintValidatorContext addConstraintViolation() {
      // TODO Auto-generated method stub
      return null;
    }
  }

  /* (non-Javadoc)
   * @see javax.validation.ConstraintValidatorContext#disableDefaultConstraintViolation()
   */
  public void disableDefaultConstraintViolation() {
    // TODO Auto-generated method stub

  }

}
