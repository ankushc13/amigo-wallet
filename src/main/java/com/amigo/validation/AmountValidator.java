package com.amigo.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AmountValidator implements ConstraintValidator<AmountValidation,Double>{

	@Override
	public boolean isValid(Double value, ConstraintValidatorContext context) {
		String regex = "^(\\d+\\.\\d{1,2})$";
		if(value==null) {
			context.disableDefaultConstraintViolation();
	        context
	            .buildConstraintViolationWithTemplate("Amount should not be null or empty")
	            .addConstraintViolation();
	        return false;
		}
		if(!value.toString().matches(regex)) {
			context.disableDefaultConstraintViolation();
	        context
	            .buildConstraintViolationWithTemplate("Amount can only contain 2 digits after the decimal")
	            .addConstraintViolation();
	        return false;
		}
		else if(value<=0) {
			context.disableDefaultConstraintViolation();
	        context
	            .buildConstraintViolationWithTemplate("Amount should be greater than 0")
	            .addConstraintViolation();
	        return false;
		}
		
		return true;
	}

}
