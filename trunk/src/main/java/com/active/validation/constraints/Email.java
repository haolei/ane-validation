package com.active.validation.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;


@Documented
@Constraint(validatedBy = {})
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@ReportAsSingleViolation
@Pattern(regexp = "")
public @interface Email {

  String message() default "{com.active.validation.constraints.Email.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  /**
   * @return an additional regular expression the annotated string must match. The default is any string ('.*')
   */
  @OverridesAttribute(constraint = Pattern.class, name = "regexp")
  String regexp() default ".*";

  /**
   * @return used in combination with {@link #regexp()} in order to specify a regular expression option
   */
  @OverridesAttribute(constraint = Pattern.class, name = "flags")
  Pattern.Flag[] flags() default {};

  /**
   * Defines several {@code @Email} annotations on the same element.
   */
  @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
  @Retention(RUNTIME)
  @Documented
  public @interface List {

    Email[] value();
  }
}
