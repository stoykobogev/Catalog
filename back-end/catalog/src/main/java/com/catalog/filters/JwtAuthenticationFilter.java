package com.catalog.filters;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.catalog.constants.SecurityConstants;
import com.catalog.services.RedisService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final RedisService redisService;
	private final AuthenticationManager authenticationManager;
	
	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, RedisService redisService) {
		this.authenticationManager = authenticationManager;
		this.redisService = redisService;
	
	    setFilterProcessesUrl(SecurityConstants.LOGIN_URL);
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
	    Authentication authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
	
	    return authenticationManager.authenticate(authenticationToken);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
	                                        FilterChain filterChain, Authentication authentication) {
	
		String username = authentication.getName();
		
	    List<String> roles = authentication.getAuthorities()
	        .stream()
	        .map(GrantedAuthority::getAuthority)
	        .collect(Collectors.toList());
	
	    byte[] signingKey = SecurityConstants.JWT_SECRET.getBytes();
	
	    String token = Jwts.builder()
	        .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
	        .setSubject(username)
	        .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.JWT_EXPIRATION_TIME))
	        .claim("roles", roles)
	        .compact();
	    
	    this.redisService.saveAuthentication(username, token);
	
	    response.addHeader(SecurityConstants.JWT_HEADER, SecurityConstants.JWT_PREFIX + token);
	}

}
