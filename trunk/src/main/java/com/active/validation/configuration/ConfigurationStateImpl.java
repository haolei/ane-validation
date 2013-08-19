package com.active.validation.configuration;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.ParameterNameProvider;
import javax.validation.TraversableResolver;
import javax.validation.spi.ConfigurationState;

public class ConfigurationStateImpl implements ConfigurationState {

	
	/**
	 * do not support for XML configuration
	 */
	public boolean isIgnoreXmlConfiguration() {
		return true;
	}

	/**
	 * delegate to ANE message resources
	 */
	public MessageInterpolator getMessageInterpolator() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<InputStream> getMappingStreams() {
		return Collections.emptySet();
	}

	public ConstraintValidatorFactory getConstraintValidatorFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	public TraversableResolver getTraversableResolver() {
		// TODO Auto-generated method stub
		return null;
	}

	public ParameterNameProvider getParameterNameProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, String> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

}
