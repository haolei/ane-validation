package com.active.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;

public class AnnotationConstraintValidatorFactory implements
		ConstraintValidatorFactory {

	public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
		// TODO Auto-generated method stub
		return null;
	}

	public void releaseInstance(ConstraintValidator<?, ?> instance) {
		// TODO Auto-generated method stub

	}

}
