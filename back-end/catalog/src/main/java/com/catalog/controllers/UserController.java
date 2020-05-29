package com.catalog.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catalog.services.RedisService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private RedisService redisService;
	
	
	@GetMapping("/logout")
	public void logout(Principal principal) {
		this.redisService.invalidateAuthentication(principal.getName());
	}
}
