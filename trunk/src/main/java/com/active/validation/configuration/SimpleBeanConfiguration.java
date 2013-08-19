package com.active.validation.configuration;

import java.io.InputStream;

import javax.validation.BootstrapConfiguration;
import javax.validation.Configuration;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;
import javax.validation.ValidatorFactory;

public class SimpleBeanConfiguration implements
		Configuration<SimpleBeanConfiguration> {

	public SimpleBeanConfiguration ignoreXmlConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	public SimpleBeanConfiguration messageInterpolator(
			MessageInterpolator interpolator) {
		// TODO Auto-generated method stub
		return null;
	}

	public SimpleBeanConfiguration traversableResolver(
			TraversableResolver resolver) {
		// TODO Auto-generated method stub
		return null;
	}

	public SimpleBeanConfiguration constraintValidatorFactory(
			ConstraintValidatorFactory constraintValidatorFactory) {
		// TODO Auto-generated method stub
		return null;
	}

	public SimpleBeanConfiguration parameterNameProvider(
			ParameterNameProvider parameterNameProvider) {
		// TODO Auto-generated method stub
		return null;
	}

	public SimpleBeanConfiguration addMapping(InputStream stream) {
		// TODO Auto-generated method stub
		return null;
	}

	public SimpleBeanConfiguration addProperty(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	public MessageInterpolator getDefaultMessageInterpolator() {
		// TODO Auto-generated method stub
		return null;
	}

	public TraversableResolver getDefaultTraversableResolver() {
		// TODO Auto-generated method stub
		return null;
	}

	public ConstraintValidatorFactory getDefaultConstraintValidatorFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	public ParameterNameProvider getDefaultParameterNameProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	public BootstrapConfiguration getBootstrapConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	public ValidatorFactory buildValidatorFactory() {
		// TODO Auto-generated method stub
		return null;
	}

}
