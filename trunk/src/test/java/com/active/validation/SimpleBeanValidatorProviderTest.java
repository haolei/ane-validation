package com.active.validation;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;

import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.active.validation.configuration.SimpleBeanConfiguration;


/**
 * 
 * @author hhao
 *
 */
public class SimpleBeanValidatorProviderTest {

  @Before
  public void setUp() throws Exception {}

  @After
  public void tearDown() throws Exception {}

  @Test
  public void testLoadResource() throws IOException {
    String provider = "META-INF/services/" + javax.validation.spi.ValidationProvider.class.getName();
    URL u = this.getClass().getClassLoader().getResource( provider );
    System.out.println( u.toExternalForm() );

  }

  @Test
  public void testBuildFactory() {
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    assertTrue( validatorFactory != null );
    assertTrue( validatorFactory instanceof ValidatorFactoryImpl );

    Validator validator = validatorFactory.getValidator();
    assertTrue(validator != null);
    assertTrue( validator instanceof ValidatorImpl );

  }

  @Test(expected = ValidationException.class)
  public void TestBuildFactoryClose() {
    SimpleBeanConfiguration configuration = Validation.byProvider( SimpleBeanValidationProvider.class ).configure().messageInterpolator( null );
    assertTrue( configuration.getMessageInterpolator() == configuration.getDefaultMessageInterpolator() );
    ValidatorFactory validatorFactory = configuration.buildValidatorFactory();
    validatorFactory.close();
    validatorFactory.getValidator();
  }
  
}

