package com.active.validation.validators;

import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import javax.validation.constraints.Pattern;


public class PatternValidator implements ConstraintValidator<Pattern, CharSequence> {

  private java.util.regex.Pattern pattern;

  public void initialize( Pattern parameters ) {
    Pattern.Flag flags[] = parameters.flags();
    int intFlag = 0;
    for ( Pattern.Flag flag : flags ) {
      intFlag = intFlag | flag.getValue();
    }

    try {
      pattern = java.util.regex.Pattern.compile( parameters.regexp(), intFlag );
    }
    catch ( PatternSyntaxException e ) {
      throw new ValidationException( "Invalid regular expression.", e );
    }
  }

  public boolean isValid( CharSequence value, ConstraintValidatorContext constraintValidatorContext ) {
    if ( value == null ) { return true; }
    Matcher m = pattern.matcher( value );
    return m.matches();
  }
}
