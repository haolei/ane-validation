/*
 * Copyright (c) 2013 Active Network Inc. All rights reserved.
 */
package com.active.validation;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import javax.validation.groups.Default;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ElementDescriptor.ConstraintFinder;
import javax.validation.metadata.PropertyDescriptor;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.active.validation.metadata.BeanMetaDataManager;
import com.active.validation.metadata.TypeHelper;


/**
 * @author hhao
 *
 */
public class ValidatorImpl implements Validator {

  private static final Log log = LogFactory.getLog( ValidatorImpl.class );

  private ValidatorFactoryImpl validatorFactory;

  private static Class<?>[] DEFAULT_GROUP = new Class<?>[]{ Default.class };

  public ValidatorImpl( ValidatorFactoryImpl impl ) {
    this.validatorFactory = impl;
  }

  /* (non-Javadoc)
   * @see javax.validation.Validator#validate(java.lang.Object, java.lang.Class[])
   */
  public <T> Set<ConstraintViolation<T>> validate( T object, Class<?>... groups ) {
    validatorFactory.checkCloseStatusWithEx();
    BeanDescriptor beanDescriptor = this.getConstraintsForClass( object.getClass() );
    Set<PropertyDescriptor> propertiesDescriptor = beanDescriptor.getConstrainedProperties();
    if ( !beanDescriptor.hasConstraints() ) { return Collections.emptySet(); }

    Set<ConstraintViolation<T>> constrains = new HashSet<ConstraintViolation<T>>();
    for ( PropertyDescriptor propertyDescriptor : propertiesDescriptor ) {
      constrains.addAll( validateProperty( object, propertyDescriptor.getPropertyName(), groups == null ? DEFAULT_GROUP : groups ) );
    }
    return constrains;
  }

  /* (non-Javadoc)
   * @see javax.validation.Validator#validateProperty(java.lang.Object, java.lang.String, java.lang.Class[])
   */
  @SuppressWarnings("unchecked")
  public <T> Set<ConstraintViolation<T>> validateProperty( T object, String propertyName, Class<?>... groups ) {
    validatorFactory.checkCloseStatusWithEx();
    BeanDescriptor beanDescriptor = this.getConstraintsForClass( object.getClass() );
    if ( !beanDescriptor.hasConstraints() ) { return Collections.emptySet(); }
    PropertyDescriptor propertyDescriptor = beanDescriptor.getConstraintsForProperty( propertyName );
    if ( !propertyDescriptor.hasConstraints() ) { return Collections.emptySet(); }

    //validate using validateValue method
    Set<ConstraintViolation<T>> constrains = new HashSet<ConstraintViolation<T>>();
    try {
      constrains = validateValue( (Class<T>)object.getClass(), propertyName, PropertyUtils.getProperty( object, propertyName ), groups == null ? DEFAULT_GROUP : groups );
    }
    catch ( Exception e ) {
      throw new ValidationException( "InvokingFail", e );
    }

    return constrains;
  }

  /* (non-Javadoc)
   * @see javax.validation.Validator#validateValue(java.lang.Class, java.lang.String, java.lang.Object, java.lang.Class[])
   */
  public <T> Set<ConstraintViolation<T>> validateValue( Class<T> beanType, String propertyName, Object value, Class<?>... groups ) {
    return validateValue( null, beanType, propertyName, value, groups );
  }

  /**
   * unified method to call validate
   * @param object
   * @param beanType
   * @param propertyName
   * @param value
   * @param groups
   * @return ConstraintViolation
   */
  public <T> Set<ConstraintViolation<T>> validateValue( T object, Class<T> beanType, String propertyName, Object value, Class<?>... groups ) {
    validatorFactory.checkCloseStatusWithEx();
    BeanDescriptor beanDescriptor = this.getConstraintsForClass( beanType );
    if ( !beanDescriptor.hasConstraints() ) { return Collections.emptySet(); }
    PropertyDescriptor propertyDescriptor = beanDescriptor.getConstraintsForProperty( propertyName );
    ConstraintFinder finder = propertyDescriptor.findConstraints().unorderedAndMatchingGroups( groups ).declaredOn( ElementType.FIELD );
    if ( !finder.hasConstraints() ) { return Collections.emptySet(); }
    ValidationExecutionContext<T> context = new ValidationExecutionContext<T>( validatorFactory.getMessageInterpolator() );
    return composingValidate( null, beanType, value, propertyDescriptor, finder.getConstraintDescriptors(), context );
  }

  /**
   * @param constraintDescriptors
   */
  @SuppressWarnings("unchecked")
  private <T, A extends Annotation, C> Set<ConstraintViolation<T>> composingValidate( T object, Class<T> clazz, Object propertyValue, PropertyDescriptor propertyDescriptor,
      Set<ConstraintDescriptor<?>> constraintDescriptors, ValidationExecutionContext<T> executionContext ) {
    for ( ConstraintDescriptor<?> descriptor : constraintDescriptors ) {

      //call itself, validate the composing constraints
      if ( !descriptor.getComposingConstraints().isEmpty() ) {
        composingValidate( object, clazz, propertyValue, propertyDescriptor, descriptor.getComposingConstraints(), executionContext );
      }
      if ( executionContext.shouldStop() ) {
        break;
      }
      ConstraintDescriptor<A> typedDescriptor = (ConstraintDescriptor<A>)descriptor;
      List<Class<? extends ConstraintValidator<A, ?>>> list = typedDescriptor.getConstraintValidatorClasses();

      Class<?> propertyClassType = propertyDescriptor.getElementClass();
      Class<ConstraintValidator<A, C>> validatorClassType = findSuitableValidator( propertyClassType, list );

      ConstraintValidator<A, C> validator = validatorFactory.getConstraintValidatorFactory().getInstance( validatorClassType );
      validator.initialize( (A)descriptor.getAnnotation() );
      boolean isValid = false;
      C typedPropertyValue = (C)propertyValue;
      isValid = validator.isValid( typedPropertyValue, new ConstraintValidatorContextImpl( descriptor ) );
      ConstraintValidatorContext validatorContext = new ConstraintValidatorContextImpl( descriptor );
      executionContext.addToExecution( validatorContext, object, clazz, propertyValue, descriptor.getMessageTemplate(), descriptor, isValid );
    }
    return executionContext.getAllConstraintVoilation();
  }

  /**
   * @param propertyClassType
   * @param list
   */
  @SuppressWarnings("unchecked")
  private <A extends Annotation, C> Class<ConstraintValidator<A, C>> findSuitableValidator( Type propertyClassType, List<Class<? extends ConstraintValidator<A, ?>>> list ) {
    Map<Type, Class<? extends ConstraintValidator<?, ?>>> availableValidatorTypes = TypeHelper.getValidatorsTypes( list );

    List<Type> discoveredSuitableTypes = findSuitableValidatorTypes( propertyClassType, availableValidatorTypes );
    resolveAssignableTypes( discoveredSuitableTypes );
    verifyResolveWasUnique( propertyClassType, discoveredSuitableTypes );

    Type suitableType = discoveredSuitableTypes.get( 0 );
    return (Class<ConstraintValidator<A, C>>)availableValidatorTypes.get( suitableType );

  }

  private List<Type> findSuitableValidatorTypes( Type type, Map<Type, Class<? extends ConstraintValidator<?, ?>>> availableValidatorTypes ) {
    List<Type> determinedSuitableTypes = new ArrayList<Type>();
    for ( Type validatorType : availableValidatorTypes.keySet() ) {
      if ( TypeHelper.isAssignable( validatorType, type ) && !determinedSuitableTypes.contains( validatorType ) ) {
        determinedSuitableTypes.add( validatorType );
      }
    }
    return determinedSuitableTypes;
  }

  /**
   * Tries to reduce all assignable classes down to a single class.
   *
   * @param assignableTypes The set of all classes which are assignable to the class of the value to be validated and
   * which are handled by at least one of the  validators for the specified constraint.
   */
  private void resolveAssignableTypes( List<Type> assignableTypes ) {
    if ( assignableTypes.size() == 0 || assignableTypes.size() == 1 ) { return; }

    List<Type> typesToRemove = new ArrayList<Type>();
    do {
      typesToRemove.clear();
      Type type = assignableTypes.get( 0 );
      for ( int i = 1; i < assignableTypes.size(); i++ ) {
        if ( TypeHelper.isAssignable( type, assignableTypes.get( i ) ) ) {
          typesToRemove.add( type );
        }
        else if ( TypeHelper.isAssignable( assignableTypes.get( i ), type ) ) {
          typesToRemove.add( assignableTypes.get( i ) );
        }
      }
      assignableTypes.removeAll( typesToRemove );
    }
    while ( typesToRemove.size() > 0 );
  }

  private void verifyResolveWasUnique( Type valueClass, List<Type> assignableClasses ) {
    if ( assignableClasses.size() == 0 ) {
      /*String className = valueClass.toString();
      if ( valueClass instanceof Class ) {
        Class<?> clazz = (Class<?>)valueClass;
        if ( clazz.isArray() ) {
          className = clazz.getComponentType().toString() + "[]";
        }
        else {
          className = clazz.getName();
        }
      }*/
      throw new ValidationException( "NoValidatorFound" );
    }
    else if ( assignableClasses.size() > 1 ) {
      StringBuilder builder = new StringBuilder();
      for ( Type clazz : assignableClasses ) {
        builder.append( clazz );
        builder.append( ", " );
      }
      builder.delete( builder.length() - 2, builder.length() );
      throw new ValidationException( "MoreThanOneValidatorFound" );
    }
  }

  /* (non-Javadoc)
   * @see javax.validation.Validator#getConstraintsForClass(java.lang.Class)
   */
  public BeanDescriptor getConstraintsForClass( Class<?> clazz ) {
    validatorFactory.checkCloseStatusWithEx();
    return BeanMetaDataManager.getBeanDescriptor( clazz );
  }

  /* (non-Javadoc)
   * @see javax.validation.Validator#unwrap(java.lang.Class)
   */
  public <T> T unwrap( Class<T> type ) {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see javax.validation.Validator#forExecutables()
   */
  public ExecutableValidator forExecutables() {
    // TODO Auto-generated method stub
    return null;
  }

}
