package com.active.validation.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstructorDescriptor;
import javax.validation.metadata.MethodDescriptor;
import javax.validation.metadata.MethodType;
import javax.validation.metadata.PropertyDescriptor;
import javax.validation.metadata.Scope;

import com.active.validation.util.ConstraintUtil;
import com.active.validation.util.ReflectionUtil;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;


public class BeanDescriptorImpl<T> extends ElementDescriptorImpl implements BeanDescriptor {

  private final ConcurrentHashMap<String, PropertyDescriptorImpl> propertyDescriptorMap;

  public BeanDescriptorImpl( Class<T> type ) {
    super( type, Collections.<ConstraintDescriptorImpl<?>> emptySet(), ConstraintUtil.getGroups( type ) );
    propertyDescriptorMap = buildPropertyDescriptorMap( type );
  }

  public boolean isBeanConstrained() {
    return !propertyDescriptorMap.isEmpty();
  }

  @Override
  public boolean hasConstraints() {
    return isBeanConstrained();
  }

  public PropertyDescriptor getConstraintsForProperty( String propertyName ) {
    return propertyDescriptorMap.get( propertyName );
  }

  public Set<PropertyDescriptor> getConstrainedProperties() {
    return ImmutableSet.<PropertyDescriptor> copyOf( propertyDescriptorMap.values() );
  }

  public MethodDescriptor getConstraintsForMethod( String methodName, Class<?>... parameterTypes ) {
    return null; // not supported
  }

  public Set<MethodDescriptor> getConstrainedMethods( MethodType methodType, MethodType... methodTypes ) {
    return null; // not supported
  }

  public ConstructorDescriptor getConstraintsForConstructor( Class<?>... parameterTypes ) {
    return null; // not supported
  }

  public Set<ConstructorDescriptor> getConstrainedConstructors() {
    return null; // not supported
  }

  private ConcurrentHashMap<String, PropertyDescriptorImpl> buildPropertyDescriptorMap( Class<T> beanClass ) {
    ConcurrentHashMap<String, PropertyDescriptorImpl> map = new ConcurrentHashMap<String, PropertyDescriptorImpl>();
    for ( Field f : ReflectionUtil.getAllFields( beanClass, Predicates.alwaysTrue() ) ) {
      Set<ConstraintDescriptorImpl<?>> propertyConstraintDescriptors = new HashSet<ConstraintDescriptorImpl<?>>();
      for ( Annotation a : f.getAnnotations() ) {
        if ( ConstraintUtil.isConstraintAnnotation( a.annotationType() ) ) {
          propertyConstraintDescriptors.add( new ConstraintDescriptorImpl<Annotation>( a,
              f.getDeclaringClass() == beanClass ? Scope.LOCAL_ELEMENT : Scope.HIERARCHY ) );
        }
      }
      if ( !propertyConstraintDescriptors.isEmpty() ) {
        List<Class<?>> empty = Collections.emptyList();
        map.put( f.getName(),
            new PropertyDescriptorImpl( f.getName(), f.getType(), propertyConstraintDescriptors, empty ) );
      }
    }

    return map;
  }

  @Override
  protected Set<ConstraintDescriptorImpl<?>> getAllConstraintDescriptors() {
    Set<ConstraintDescriptorImpl<?>> result = new HashSet<ConstraintDescriptorImpl<?>>();
    for ( PropertyDescriptorImpl pd : propertyDescriptorMap.values() ) {
      result.addAll( pd.getAllConstraintDescriptors() );
    }
    return result;
  }
}
