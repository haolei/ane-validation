package com.active.validation.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.validation.Constraint;
import javax.validation.GroupSequence;

import com.active.validation.metadata.ConstraintRegister;


public class ConstraintUtil {

  public static boolean isConstraintAnnotation( Class<? extends Annotation> annotationType ) {
    return annotationType.isAnnotationPresent( Constraint.class );
  }

  /**
   * Checks whether a given annotation is a multi value constraint or not.
   *
   * @param annotationType the annotation type to check.
   *
   * @return {@code true} if the specified annotation is a multi value constraints, {@code false}
   *         otherwise.
   */
  public static boolean isMultiValueConstraint( Class<? extends Annotation> annotationType ) {
    boolean isMultiValueConstraint = false;
    Method method = null;
    try {
      method = annotationType.getMethod( "value" );
    }
    catch ( Exception e ) {
    }

    if ( method != null ) {
      Class<?> returnType = method.getReturnType();
      if ( returnType.isArray() && returnType.getComponentType().isAnnotation() ) {
        @SuppressWarnings("unchecked")
        Class<? extends Annotation> componentType = (Class<? extends Annotation>)returnType.getComponentType();
        if ( isConstraintAnnotation( componentType ) || ConstraintRegister.isBuiltinConstraint( componentType ) ) {
          isMultiValueConstraint = true;
        }
        else {
          isMultiValueConstraint = false;
        }
      }
    }
    return isMultiValueConstraint;
  }

  /**
   * Checks whether a given annotation is a multi value constraint and returns the contained constraints if so.
   *
   * @param annotation the annotation to check.
   *
   * @return A list of constraint annotations or the empty list if <code>annotation</code> is not a multi constraint
   *         annotation.
   */
  public static <A extends Annotation> List<Annotation> getMultiValueConstraints( A annotation ) {
    List<Annotation> annotationList = new ArrayList<Annotation>();
    try {
      final Method method = annotation.annotationType().getMethod( "value" );
      if ( method != null ) {
        Class<?> returnType = method.getReturnType();
        if ( returnType.isArray() && returnType.getComponentType().isAnnotation() ) {
          Annotation[] annotations = (Annotation[])method.invoke( annotation );
          for ( Annotation a : annotations ) {
            Class<? extends Annotation> annotationType = a.annotationType();
            if ( isConstraintAnnotation( annotationType ) || ConstraintRegister.isBuiltinConstraint( annotationType ) ) {
              annotationList.add( a );
            }
          }
        }
      }
    }
    catch ( Exception iae ) {
      // ignore
    }
    return annotationList;
  }

  public static List<Class<?>> getGroups( Class<?> type ) {
    if ( !type.isAnnotationPresent( GroupSequence.class ) ) { return Collections.emptyList(); }
    Class<?>[] groups = type.getAnnotation( GroupSequence.class ).value();
    return Arrays.asList( groups );
  }

}
