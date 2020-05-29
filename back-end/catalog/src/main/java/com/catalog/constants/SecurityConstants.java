package com.catalog.constants;

public final class SecurityConstants {

	private SecurityConstants() {}
	
	public static final String LOGIN_URL = "/api/login";
    public static final String JWT_SECRET = "n2r5u8x/A%D*G-KaPdSgVkYp3s6v9y$B&E(H+MbQeThWmZq4t7w!z%C*F-J@NcRf";
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";
    public static final int JWT_EXPIRATION_TIME = 1000 * 60 * 60;

}
