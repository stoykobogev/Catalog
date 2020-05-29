package com.catalog.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.catalog.dtos.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class AuthenticationHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) 
    		throws IOException, ServletException {
    	
    	UserDto dto = new UserDto();
        
    	dto.setUsername(authentication.getName());
    	dto.setRoles(authentication.getAuthorities().stream()
    			.map(role -> role.getAuthority())
    			.collect(Collectors.toList()));
    	
    	this.objectMapper.writeValue(httpServletResponse.getOutputStream(), dto);
    	httpServletResponse.setStatus(HttpStatus.OK.value());
    }
}