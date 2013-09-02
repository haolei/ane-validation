/*
 * Copyright (c) 2013 Active Network Inc. All rights reserved.
 */
package com.active.validation.meta;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.constraints.NotNull;

import org.junit.Test;

import com.active.validation.metadata.TypeHelper;


/**
 * @author hhao
 *
 */
public class TypeHelperTest {

  @Test
  public void testGetValidatorType() {
    List<Class<? extends ConstraintValidator<NotNull, ?>>> list = new ArrayList<Class<? extends ConstraintValidator<NotNull, ?>>>();
    list.add( NotNullConstraintValidator.class);
    TypeHelper.getValidatorsTypes( list );
  }
}
