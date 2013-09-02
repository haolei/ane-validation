/*
 * Copyright (c) 2013 Active Network Inc. All rights reserved.
 */
package com.active.validation;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;

import com.active.validation.configuration.PropertiesEnum;


/**
 * base test class
 * @author hhao
 *
 */

public class BaseTest {

  public Validator validator;

  @Before
  public void setUp() throws Exception {
    ValidatorFactory validatorFactory = Validation.byDefaultProvider().configure().addProperty( PropertiesEnum.INTERPOLATE_MESSAGE.getKey(), "False" ).buildValidatorFactory();
    validator = validatorFactory.getValidator();
  }
}
