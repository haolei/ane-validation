package com.active.validation;

import java.lang.annotation.ElementType;

import javax.validation.ElementKind;
import javax.validation.Path;
import javax.validation.Path.Node;
import javax.validation.TraversableResolver;


public class DefaultTraversableResolver implements TraversableResolver {

  /**
   * all properties are reachable
   */
  public boolean isReachable( Object traversableObject, Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType ) {
    //only property is reachable
    if ( traversableObject == null || traversableProperty.getName() == null ) { return true; }

    if ( traversableProperty.getKind() != ElementKind.PROPERTY ) return false;

    return true;
  }

  /**
   * cascade is not supported
   * @Valid annotation is not supported
   */
  public boolean isCascadable( Object traversableObject, Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType ) {
    return false;
  }

}
