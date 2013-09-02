package com.active.validation;

import javax.validation.Configuration;
import javax.validation.ValidatorFactory;
import javax.validation.spi.BootstrapState;
import javax.validation.spi.ConfigurationState;
import javax.validation.spi.ValidationProvider;

import com.active.validation.configuration.SimpleBeanConfiguration;


/**
 * this simple validation provider only provide the simplest way to initialize a configuration,
 * user defined ValidationProviderResolver is ignored.
 * @author holly
 *
 */
public class SimpleBeanValidationProvider implements ValidationProvider<SimpleBeanConfiguration> {

  private SimpleBeanConfiguration configuration;

  public SimpleBeanConfiguration createSpecializedConfiguration( BootstrapState state ) {
    configuration = new SimpleBeanConfiguration();
    return configuration;
  }

  /**
   * call by default provider
   * <p> ignore the BootstrapState 
   */
  public Configuration<SimpleBeanConfiguration> createGenericConfiguration( BootstrapState state ) {
    return createSpecializedConfiguration( state );
  }

  public ValidatorFactory buildValidatorFactory( ConfigurationState configurationState ) {
    if ( configuration == null ) {
      configuration = new SimpleBeanConfiguration( configurationState );
    }
    return configuration.buildValidatorFactory();
  }

}
