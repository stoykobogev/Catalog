package com.catalog.handlers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.catalog.services.RedisService;

public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {
	
	private final RedisService redisService;
	
	public JwtLogoutSuccessHandler(RedisService redisService) {
		this.redisService = redisService;
	}

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		this.redisService.invalidateAuthentication(authentication.getName());
	}

}
