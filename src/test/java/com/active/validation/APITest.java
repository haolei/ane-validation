package com.active.validation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class APITest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Address address = new Address();
		address.setAddressline1( null );
		address.setAddressline2( null );
		address.setCity("Llanfairpwllgwyngyllgogerychwyrndrobwyll-llantysiliogogogoch");
		Validator validator  =  Validation.buildDefaultValidatorFactory().getValidator();
		validator.validate(address);
		
	}

}
