package com.catalog.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = ImageMimeTypeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageMimeType {

	String message() default "End date must be after begin date and both must be in the future";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}
