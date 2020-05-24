package com.catalog.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.tika.Tika;
import org.springframework.stereotype.Component;

import com.catalog.constants.AppConstants;

@Component
public class ImageMimeTypeValidator implements ConstraintValidator<ImageMimeType, byte[]> {
	
	@Override
	public boolean isValid(byte[] value, ConstraintValidatorContext context) {
		
		if (value == null) {
			return true;
		}
		
		Tika tika = new Tika();
		
		String mimeType = tika.detect(value);
		
		return AppConstants.ALLOWED_IMAGE_TYPES_SET.contains(mimeType);
	}
}
