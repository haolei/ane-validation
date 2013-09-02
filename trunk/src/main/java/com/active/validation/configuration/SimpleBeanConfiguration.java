package com.active.validation.configuration;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.validation.BootstrapConfiguration;
import javax.validation.Configuration;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;
import javax.validation.ValidatorFactory;
import javax.validation.spi.ConfigurationState;

import com.active.validation.ConstraintValidatorFactoryImpl;
import com.active.validation.DefaultTraversableResolver;
import com.active.validation.MessageInterpolatorImpl;
import com.active.validation.ValidatorFactoryImpl;


/**
 * 
 * @author hhao
 *
 */
public class SimpleBeanConfiguration implements Configuration<SimpleBeanConfiguration>, ConfigurationState {

  //generally use the default MessageInterpolator
  private MessageInterpolator messageInterpolator;

  private MessageInterpolator defaultMessageInterpolator;

  private TraversableResolver defaultTraversableResolver = new DefaultTraversableResolver();

  private ConstraintValidatorFactory constraintValidatorFactory;

  private ConstraintValidatorFactory defaultConstrainValidatorFactory = new ConstraintValidatorFactoryImpl();

  private Map<String, String> propertiesMap = new HashMap<String, String>();

  private ParameterNameProvider defaultParameterNameProvider = new ParameterNameProvider() {

    public List<String> getParameterNames( Constructor<?> constructor ) {
      return Collections.emptyList();
    }

    public List<String> getParameterNames( Method method ) {
      return Collections.emptyList();
    }
  };

  /**
   * constructor 
   */
  public SimpleBeanConfiguration() {
    initDefaultProperty();
  }

  /**
   * @param configurationState
   */
  public SimpleBeanConfiguration( ConfigurationState configurationState ) {
    this();
    constraintValidatorFactory = configurationState.getConstraintValidatorFactory();
    messageInterpolator = configurationState.getMessageInterpolator();
    propertiesMap.putAll( configurationState.getProperties() );
  }

  /**
   * initialize the default property
   */
  private void initDefaultProperty() {
    PropertiesEnum.addAll( propertiesMap );

  }

  /**
   * default xml is not support
   */
  public SimpleBeanConfiguration ignoreXmlConfiguration() {
    return this;
  }

  public SimpleBeanConfiguration messageInterpolator( MessageInterpolator interpolator ) {
    messageInterpolator = interpolator;
    return this;
  }

  /**
   * traversableResolver is not support
   */
  public SimpleBeanConfiguration traversableResolver( TraversableResolver resolver ) {
    return this;
  }

  public SimpleBeanConfiguration constraintValidatorFactory( ConstraintValidatorFactory constraintValidatorFactory ) {
    this.constraintValidatorFactory = constraintValidatorFactory;
    return this;
  }

  /**
   * method and constructor validation is not support 
   */
  public SimpleBeanConfiguration parameterNameProvider( ParameterNameProvider parameterNameProvider ) {
    return this;
  }

  /**
   * not support
   */
  public SimpleBeanConfiguration addMapping( InputStream stream ) {
    return this;
  }

  public SimpleBeanConfiguration addProperty( String name, String value ) {
    this.propertiesMap.put( name, value );
    return this;
  }

  public MessageInterpolator getDefaultMessageInterpolator() {
    //if false
    if ( !Boolean.parseBoolean( propertiesMap.get( PropertiesEnum.INTERPOLATE_MESSAGE.getKey() ) ) ) {
      new MessageInterpolator() {

        public String interpolate( String messageTemplate, Context context ) {
          return messageTemplate;
        }

        public String interpolate( String messageTemplate, Context context, Locale locale ) {
          return messageTemplate;
        }
      };
    }
    if ( defaultMessageInterpolator == null ) defaultMessageInterpolator = new MessageInterpolatorImpl( this );
    return defaultMessageInterpolator;
  }

  public TraversableResolver getDefaultTraversableResolver() {

    return defaultTraversableResolver;
  }

  public ConstraintValidatorFactory getDefaultConstraintValidatorFactory() {
    return defaultConstrainValidatorFactory;
  }

  public ParameterNameProvider getDefaultParameterNameProvider() {
    return defaultParameterNameProvider;
  }

  public BootstrapConfiguration getBootstrapConfiguration() {
    // TODO Auto-generated method stub
    return null;
  }

  public ValidatorFactory buildValidatorFactory() {
    return new ValidatorFactoryImpl( this );
  }

  public boolean isIgnoreXmlConfiguration() {
    return true;
  }

  public MessageInterpolator getMessageInterpolator() {
    return messageInterpolator == null ? this.getDefaultMessageInterpolator() : messageInterpolator;
  }

  public Set<InputStream> getMappingStreams() {
    return Collections.emptySet();
  }

  public ConstraintValidatorFactory getConstraintValidatorFactory() {
    if ( constraintValidatorFactory == null ) { return defaultConstrainValidatorFactory; }
    return constraintValidatorFactory;
  }

  public TraversableResolver getTraversableResolver() {
    return defaultTraversableResolver;
  }

  public ParameterNameProvider getParameterNameProvider() {
    return defaultParameterNameProvider;
  }

  public Map<String, String> getProperties() {
    return this.propertiesMap;
  }

}
