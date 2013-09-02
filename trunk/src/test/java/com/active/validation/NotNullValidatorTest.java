/*
 * Copyright (c) 2013 Active Network Inc. All rights reserved.
 */
package com.active.validation;

import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.junit.Test;


/**
 * @author hhao
 *
 */
public class NotNullValidatorTest extends BaseTest {

  @Test
  public void testNotNull() {
    Address address = new Address();
    Set<ConstraintViolation<Address>> set = validator.validateProperty( address, "zipCode" );

    assertTrue( set.size() == 1 );
    System.out.println( set.iterator().next().getMessageTemplate() );
    System.out.println("----------");
    address.setZipCode( "111" );
    set = validator.validateProperty( address, "zipCode" );
    assertTrue( set.size() == 0 );
    address.setCity( "123" );
    set = validator.validateProperty( address, "city" );
    assertTrue( set.size() == 2 );
    Iterator<ConstraintViolation<Address>> it = set.iterator();
    System.out.println( it.next().getMessageTemplate() );
    System.out.println( it.next().getMessageTemplate() );
    System.out.println("----------");
    address.setEmail( "notcorrect@" );
    set = validator.validateProperty( address, "email" );
    assertTrue( set.size() == 1 );
    System.out.println( set.iterator().next().getMessageTemplate() );
    System.out.println("----------");
    address.setEmail( "correct@active.com" );
    set = validator.validateProperty( address, "email" );
    assertTrue( set.size() == 0 );
  }
}
