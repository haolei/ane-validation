package com.active.validation.metadata;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.validation.ConstraintValidator;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.active.validation.constraints.Email;
import com.active.validation.util.ColUtil;
import com.active.validation.validators.EmailValidator;
import com.active.validation.validators.FutureValidatorForCalendar;
import com.active.validation.validators.FutureValidatorForDate;
import com.active.validation.validators.MaxValidatorForCharSequence;
import com.active.validation.validators.MaxValidatorForNumber;
import com.active.validation.validators.MinValidatorForCharSequence;
import com.active.validation.validators.MinValidatorForNumber;
import com.active.validation.validators.NotNullValidator;
import com.active.validation.validators.NullValidator;
import com.active.validation.validators.PastValidatorForCalendar;
import com.active.validation.validators.PastValidatorForDate;
import com.active.validation.validators.PatternValidator;
import com.active.validation.validators.SizeValidatorForCharSequence;


public class ConstraintRegister {

  private static final ConcurrentHashMap<Class<? extends Annotation>, List<Class<? extends ConstraintValidator<? extends Annotation, ?>>>> builtinConstraints = new ConcurrentHashMap<Class<? extends Annotation>, List<Class<? extends ConstraintValidator<? extends Annotation, ?>>>>();
  static {
    List<Class<? extends ConstraintValidator<? extends Annotation, ?>>> constraintList = Collections.emptyList();
    builtinConstraints.put( AssertFalse.class, constraintList );

    constraintList = Collections.emptyList();
    builtinConstraints.put( AssertTrue.class, constraintList );

    constraintList = Collections.emptyList();
    builtinConstraints.put( DecimalMax.class, constraintList );

    constraintList = Collections.emptyList();
    builtinConstraints.put( DecimalMin.class, constraintList );

    constraintList = Collections.emptyList();
    builtinConstraints.put( Digits.class, constraintList );

    constraintList = ColUtil.newArrayList();
    constraintList.add( EmailValidator.class );
    builtinConstraints.put( Email.class, constraintList );

    constraintList = ColUtil.newArrayList();
    constraintList.add( FutureValidatorForDate.class );
    constraintList.add( FutureValidatorForCalendar.class );
    builtinConstraints.put( Future.class, constraintList );

    constraintList = ColUtil.newArrayList();
    constraintList.add( MaxValidatorForCharSequence.class );
    constraintList.add( MaxValidatorForNumber.class );
    builtinConstraints.put( Max.class, constraintList );

    constraintList = ColUtil.newArrayList();
    constraintList.add( MinValidatorForCharSequence.class );
    constraintList.add( MinValidatorForNumber.class );
    builtinConstraints.put( Min.class, constraintList );

    constraintList = ColUtil.newArrayList();
    constraintList.add( NullValidator.class );
    builtinConstraints.put( Null.class, constraintList );

    constraintList = ColUtil.newArrayList();
    constraintList.add( NotNullValidator.class );
    builtinConstraints.put( NotNull.class, constraintList );

    constraintList = ColUtil.newArrayList();
    constraintList.add( PastValidatorForDate.class );
    constraintList.add( PastValidatorForCalendar.class );
    builtinConstraints.put( Past.class, constraintList );

    constraintList = ColUtil.newArrayList();
    constraintList.add( PatternValidator.class );
    builtinConstraints.put( Pattern.class, constraintList );

    constraintList = ColUtil.newArrayList();
    constraintList.add( SizeValidatorForCharSequence.class );
    builtinConstraints.put( Size.class, constraintList );

  }

  private final static ConcurrentHashMap<Class<? extends Annotation>, List<Class<? extends ConstraintValidator<? extends Annotation, ?>>>> constraintsCache = new ConcurrentHashMap<Class<? extends Annotation>, List<Class<? extends ConstraintValidator<? extends Annotation, ?>>>>();

  public static Set<Class<? extends Annotation>> constraintAnnotations() {
    return builtinConstraints.keySet();
  }

  public static List<Class<? extends ConstraintValidator<? extends Annotation, ?>>> getBuiltInConstraints(
      Class<? extends Annotation> annotationClass ) {
    return builtinConstraints.get( annotationClass );
  }

  public static boolean isBuiltinConstraint( Class<? extends Annotation> annotationType ) {
    return builtinConstraints.containsKey( annotationType );
  }

  public static boolean isConstraintsCached( Class<? extends Annotation> annotationType ) {
    return constraintsCache.containsKey( annotationType );
  }

  @SuppressWarnings("unchecked")
  public static <T extends Annotation> List<Class<? extends ConstraintValidator<T, ?>>> getConstraintValidators(
      Class<T> annotationClass ) {
    List<Class<? extends ConstraintValidator<T, ?>>> list = new ArrayList<Class<? extends ConstraintValidator<T, ?>>>();
    for ( Class<? extends ConstraintValidator<? extends Annotation, ?>> cvClass : constraintsCache
        .get( annotationClass ) ) {
      list.add( (Class<? extends ConstraintValidator<T, ?>>)cvClass );
    }

    return list;
  }

  public static <T extends Annotation> void addConstraintValidators( Class<T> annotationClass,
      List<Class<? extends ConstraintValidator<? extends Annotation, ?>>> validators ) {
    constraintsCache.putIfAbsent( annotationClass, validators );
  }
}
