package com.active.validation;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.validation.MessageInterpolator;
import javax.validation.MessageInterpolator.Context;
import javax.validation.metadata.ConstraintDescriptor;

import com.active.validation.configuration.SimpleBeanConfiguration;


/**
 * 
 * @author hhao
 *
 */

public class MessageInterpolatorImpl implements MessageInterpolator {
  public static final String MESSAGE_BUNDLE_PREFIX = "MessagesBundle";

  private Locale defaultLocale;

  private ResourceBundle defaultResourceBundle;

  private SimpleBeanConfiguration simpleBeanConfiguration;

  public MessageInterpolatorImpl() {
    defaultLocale = Locale.getDefault();
  }

  /**
   * @param simpleBeanConfiguration
   */
  public MessageInterpolatorImpl( SimpleBeanConfiguration simpleBeanConfiguration ) {
    this.simpleBeanConfiguration = simpleBeanConfiguration;
  }

  public String interpolate( String messageTemplate, Context context ) {
    return interpolate( messageTemplate, context, defaultLocale );
  }

  public String interpolate( String messageTemplate, Context context, Locale locale ) {
    //    new MessageFormat()
    ResourceBundle resourceBundle = ResourceBundle.getBundle( MESSAGE_BUNDLE_PREFIX, locale );

    if ( resourceBundle == null ) {
      resourceBundle = ResourceBundle.getBundle( MESSAGE_BUNDLE_PREFIX, defaultLocale );
    }

    if ( resourceBundle == null ) { return messageTemplate; }
    //:TODO support parameters parsing
    return defaultResourceBundle.getString( messageTemplate );
  }


}

class MessageInterpolatorContextImpl implements Context {

  private ConstraintDescriptor<?> descriptor;
  private Object value;

  public MessageInterpolatorContextImpl( ConstraintDescriptor<?> descriptor, Object value ) {
    this.descriptor = descriptor;
    this.value = value;
  }
  public ConstraintDescriptor<?> getConstraintDescriptor() {
    return descriptor;
  }

  public Object getValidatedValue() {
    return value;
  }

  /* (non-Javadoc)
   * @see javax.validation.MessageInterpolator.Context#unwrap(java.lang.Class)
   */
  public <T> T unwrap( Class<T> type ) {
    // TODO Auto-generated method stub
    return null;
  }
}
