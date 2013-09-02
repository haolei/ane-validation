package com.active.validation.metadata;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Constraint;
import javax.validation.ConstraintTarget;
import javax.validation.ConstraintValidator;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.groups.Default;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.Scope;

import com.active.validation.util.AnnotationUtil;
import com.active.validation.util.ConstraintUtil;


public class ConstraintDescriptorImpl<T extends Annotation> implements ConstraintDescriptor<T> {

  private static final String GROUPS = "groups";
  private static final String MESSAGE = "message";
  private static final int OVERRIDES_PARAMETER_DEFAULT_INDEX = -1;

  /**
   * The actual constraint annotation.
   */
  private final T annotation;

  /**
   * The type of the annotation made instance variable, because {@code annotation.annotationType()} is quite expensive.
   */
  private final Class<T> annotationType;

  /**
   * The set of classes implementing the validation for this constraint. See also
   * {@code ConstraintValidator} resolution algorithm.
   */
  private final List<Class<? extends ConstraintValidator<T, ?>>> constraintValidatorDefinitionClasses;

  /**
   * The groups for which to apply this constraint.
   */
  private final Set<Class<?>> groups;

  /**
   * Flag indicating if in case of a composing constraint a single error or multiple errors should be raised.
   */
  private final boolean isReportAsSingleInvalidConstraint;

  private final Scope definedOn;

  private final ElementType elementType = ElementType.FIELD;

  private final String message;

  /**
   * The constraint parameters as map. The key is the parameter name and the value the
   * parameter value as specified in the constraint.
   */
  private final Map<String, Object> attributes;

  /**
   * The composing constraints for this constraint.
   */
  private final Set<ConstraintDescriptor<?>> composingConstraints;

  @SuppressWarnings("unchecked")
  public ConstraintDescriptorImpl( T annotation, Scope definedOn ) {
    this.annotation = annotation;
    this.annotationType = (Class<T>)this.annotation.annotationType();
    this.isReportAsSingleInvalidConstraint = annotationType.isAnnotationPresent( ReportAsSingleViolation.class );
    this.definedOn = definedOn;
    this.groups = buildGroupSet();
    this.constraintValidatorDefinitionClasses = findConstraintValidatorClasses();
    this.message = buildMessage();
    this.attributes = buildAttributes();
    this.composingConstraints = parseComposingConstraints();
  }

  public T getAnnotation() {
    return annotation;
  }

  public String getMessageTemplate() {
    return message;
  }

  public Set<Class<?>> getGroups() {
    return groups;
  }

  public Set<Class<? extends Payload>> getPayload() {
    return null; // not supported
  }

  public ConstraintTarget getValidationAppliesTo() {
    return ConstraintTarget.IMPLICIT; // always on field
  }

  public List<Class<? extends ConstraintValidator<T, ?>>> getConstraintValidatorClasses() {
    return constraintValidatorDefinitionClasses;
  }

  public Map<String, Object> getAttributes() {
    return attributes;
  }

  public Set<ConstraintDescriptor<?>> getComposingConstraints() {
    return composingConstraints;
  }

  public boolean isReportAsSingleViolation() {
    return isReportAsSingleInvalidConstraint;
  }

  private Set<Class<?>> buildGroupSet() {
    Set<Class<?>> groupSet = new HashSet<Class<?>>();
    final Class<?>[] groupsFromAnnotation = AnnotationUtil.getAnnotationParameter( annotation, GROUPS, Class[].class );
    if ( groupsFromAnnotation.length == 0 ) {
      groupSet.add( Default.class );
    }
    else {
      groupSet.addAll( Arrays.asList( groupsFromAnnotation ) );
    }

    return Collections.unmodifiableSet( groupSet );
  }

  private String buildMessage() {
    return AnnotationUtil.getAnnotationParameter( annotation, MESSAGE, String.class );
  }

  private List<Class<? extends ConstraintValidator<T, ?>>> findConstraintValidatorClasses() {
    final List<Class<? extends ConstraintValidator<T, ?>>> constraintValidatorClasses = new ArrayList<Class<? extends ConstraintValidator<T, ?>>>();
    if ( ConstraintRegister.isConstraintsCached( annotationType ) ) {
      constraintValidatorClasses.addAll( ConstraintRegister.getConstraintValidators( annotationType ) );
      return Collections.unmodifiableList( constraintValidatorClasses );
    }

    List<Class<? extends ConstraintValidator<? extends Annotation, ?>>> constraintDefinitionClasses = new ArrayList<Class<? extends ConstraintValidator<? extends Annotation, ?>>>();
    if ( ConstraintRegister.isBuiltinConstraint( annotationType ) ) {
      constraintDefinitionClasses.addAll( ConstraintRegister.getBuiltInConstraints( annotationType ) );
    }
    else {
      Class<? extends ConstraintValidator<?, ?>>[] validatedBy = annotationType.getAnnotation( Constraint.class )
          .validatedBy();
      constraintDefinitionClasses.addAll( Arrays.asList( validatedBy ) );
    }

    ConstraintRegister.addConstraintValidators( annotationType, constraintDefinitionClasses );

    for ( Class<? extends ConstraintValidator<? extends Annotation, ?>> validator : constraintDefinitionClasses ) {
      @SuppressWarnings("unchecked")
      Class<? extends ConstraintValidator<T, ?>> safeValidator = (Class<? extends ConstraintValidator<T, ?>>)validator;
      constraintValidatorClasses.add( safeValidator );
    }
    return Collections.unmodifiableList( constraintValidatorClasses );
  }

  public Scope getDefinedOn() {
    return definedOn;
  }

  public ElementType getElementType() {
    return elementType;
  }

  private Map<String, Object> buildAttributes() {
    return Collections.unmodifiableMap( AnnotationUtil.getAnnotationParameters( annotation ) );
  }

  private Set<ConstraintDescriptor<?>> parseComposingConstraints() {
    Set<ConstraintDescriptor<?>> composingConstraintsSet = new HashSet<ConstraintDescriptor<?>>();
    Map<ClassIndexWrapper, Map<String, Object>> overrideParameters = parseOverrideParameters();

    for ( Annotation declaredAnnotation : annotationType.getDeclaredAnnotations() ) {
      Class<? extends Annotation> declaredAnnotationType = declaredAnnotation.annotationType();

      if ( !declaredAnnotationType.isAnnotationPresent( Constraint.class ) ) {
        continue; // only care constraints
      }

      if ( ConstraintRegister.isBuiltinConstraint( declaredAnnotationType ) ) {
        ConstraintDescriptorImpl<?> descriptor = createComposingConstraintDescriptor( declaredAnnotation,
            overrideParameters, OVERRIDES_PARAMETER_DEFAULT_INDEX );
        if ( descriptor != null ) {
          composingConstraintsSet.add( descriptor );
        }
      }
      else if ( ConstraintUtil.isMultiValueConstraint( declaredAnnotationType ) ) {
        List<Annotation> multiValueConstraints = ConstraintUtil.getMultiValueConstraints( declaredAnnotation );
        int index = 0;
        for ( Annotation constraintAnnotation : multiValueConstraints ) {
          ConstraintDescriptorImpl<?> descriptor = createComposingConstraintDescriptor( constraintAnnotation,
              overrideParameters, index );
          if ( descriptor != null ) {
            composingConstraintsSet.add( descriptor );
          }
          index++;
        }
      }
    }
    return Collections.unmodifiableSet( composingConstraintsSet );
  }

  private <U extends Annotation> ConstraintDescriptorImpl<U> createComposingConstraintDescriptor( U declaredAnnotation,
      Map<ClassIndexWrapper, Map<String, Object>> overrideParameters, int index ) {
    @SuppressWarnings("unchecked")
    final Class<U> annotationType = (Class<U>)declaredAnnotation.annotationType();
    return createComposingConstraintDescriptor( overrideParameters, index, annotationType );
  }

  private <U extends Annotation> ConstraintDescriptorImpl<U> createComposingConstraintDescriptor(
      Map<ClassIndexWrapper, Map<String, Object>> overrideParameters, int index, Class<U> annotationType ) {
    // get the right override parameters
    Map<String, Object> overrides = overrideParameters.get( new ClassIndexWrapper( annotationType, index ) );
    if ( overrides == null ) { return null; }

    return new ConstraintDescriptorImpl<U>( AnnotationUtil.newAnnotationInstance( annotationType, overrides ),
        definedOn );
  }

  private Map<ClassIndexWrapper, Map<String, Object>> parseOverrideParameters() {
    Map<ClassIndexWrapper, Map<String, Object>> overrideParameters = new HashMap<ClassIndexWrapper, Map<String, Object>>();
    final Method[] methods = annotationType.getDeclaredMethods();
    for ( Method m : methods ) {
      if ( m.getAnnotation( OverridesAttribute.class ) != null ) {
        addOverrideAttributes( overrideParameters, m, m.getAnnotation( OverridesAttribute.class ) );
      }
      else if ( m.getAnnotation( OverridesAttribute.List.class ) != null ) {
        addOverrideAttributes( overrideParameters, m, m.getAnnotation( OverridesAttribute.List.class ).value() );
      }
    }
    return overrideParameters;
  }

  private void addOverrideAttributes( Map<ClassIndexWrapper, Map<String, Object>> overrideParameters, Method m,
      OverridesAttribute... attributes ) {

    Object value = AnnotationUtil.getAnnotationParameter( annotation, m );
    for ( OverridesAttribute overridesAttribute : attributes ) {
      ClassIndexWrapper wrapper = new ClassIndexWrapper( overridesAttribute.constraint(),
          overridesAttribute.constraintIndex() );
      Map<String, Object> map = overrideParameters.get( wrapper );
      if ( map == null ) {
        map = new HashMap<String, Object>();
        overrideParameters.put( wrapper, map );
      }
      map.put( overridesAttribute.name(), value );
    }
  }

  /**
   * A wrapper class to keep track for which composing constraints (class and index) a given attribute override applies to.
   */
  private class ClassIndexWrapper {

    final Class<?> clazz;
    final int index;

    ClassIndexWrapper( Class<?> clazz, int index ) {
      this.clazz = clazz;
      this.index = index;
    }

    @Override
    public boolean equals( Object o ) {
      if ( this == o ) { return true; }
      if ( o == null || getClass() != o.getClass() ) { return false; }

      @SuppressWarnings("unchecked")
      // safe due to the check above
      ClassIndexWrapper that = (ClassIndexWrapper)o;

      if ( index != that.index ) { return false; }
      if ( clazz != null && !clazz.equals( that.clazz ) ) { return false; }
      if ( clazz == null && that.clazz != null ) { return false; }

      return true;
    }

    @Override
    public int hashCode() {
      int result = clazz != null ? clazz.hashCode() : 0;
      result = 31 * result + index;
      return result;
    }
  }

}
