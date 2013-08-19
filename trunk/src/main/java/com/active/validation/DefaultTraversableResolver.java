package com.active.validation;

import java.lang.annotation.ElementType;

import javax.validation.Path;
import javax.validation.TraversableResolver;
import javax.validation.Path.Node;

public class DefaultTraversableResolver implements TraversableResolver {

	public boolean isReachable(Object traversableObject,
			Node traversableProperty, Class<?> rootBeanType,
			Path pathToTraversableObject, ElementType elementType) {
		return true;
	}

	/**
	 * cascade is not supported
	 * @Valid annotation is not supported
	 */
	public boolean isCascadable(Object traversableObject,
			Node traversableProperty, Class<?> rootBeanType,
			Path pathToTraversableObject, ElementType elementType) {
		return false;
	}

}
