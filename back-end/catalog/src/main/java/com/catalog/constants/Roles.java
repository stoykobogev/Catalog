package com.catalog.constants;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class Roles {
	
	private Roles() {}
	
	public static final String ADMIN = "ADMIN";
	public static final String USER = "USER";
	
	public static final Set<String> ALL = Collections.unmodifiableSet(new HashSet<String>() {
		private static final long serialVersionUID = 6769292990531943624L;
	{
		add(ADMIN);
		add(USER);
	}});
}
