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

	String message() default "File is not an image or is not in the allowed formats";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}
