package com.catalog.constants;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class AppConstants {
	
	private AppConstants() {}
	
	public static final Set<String> ALLOWED_IMAGE_TYPES_SET = Collections.unmodifiableSet(new HashSet<String>() {
		private static final long serialVersionUID = -8568479990620432362L;
	{
		add("image/jpeg");
		add("image/png");
		add("image/webp");
	}});
}
