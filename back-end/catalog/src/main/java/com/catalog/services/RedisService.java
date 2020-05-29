package com.catalog.services;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.catalog.constants.SecurityConstants;
import com.catalog.exceptions.InvalidJsonWebToken;

@Service
public class RedisService {

	@Autowired
	private StringRedisTemplate template;
	
	
	public void saveAuthentication(String username, String jwt) {
	    template.opsForValue().set(username, jwt);
	    template.expire(username, SecurityConstants.JWT_EXPIRATION_TIME, TimeUnit.MILLISECONDS);
	}
	
	public void validateAuthentication(String username) {
		if (template.opsForValue().get(username) == null) {
			throw new InvalidJsonWebToken();
		};
	}
	
	public void invalidateAuthentication(String username) {
		template.delete(username);
	}
}
