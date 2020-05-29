package com.catalog.filters;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.catalog.constants.SecurityConstants;
import com.catalog.exceptions.InvalidJsonWebToken;
import com.catalog.services.RedisService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
	
	private final RedisService redisService;
	
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, RedisService redisService) {
        super(authenticationManager);
        this.redisService = redisService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws  ServletException, IOException {
    	
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if (authentication != null) {
        	SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    	
        String token = request.getHeader(SecurityConstants.JWT_HEADER);
        
        if (StringUtils.isNotEmpty(token) && token.startsWith(SecurityConstants.JWT_PREFIX)) {
  
            byte[] signingKey = SecurityConstants.JWT_SECRET.getBytes();

            Claims claims;
            try {
            	claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token.replace(SecurityConstants.JWT_PREFIX, ""))
                .getBody();
            } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException 
            		| SignatureException | IllegalArgumentException e) {
            	throw new InvalidJsonWebToken();
            }
            
            String username = claims.getSubject();
            
            this.redisService.validateAuthentication(username);

            @SuppressWarnings("unchecked")
			List<SimpleGrantedAuthority> authorities = ((List<String>) claims.get("roles", List.class))              		
        		.stream()
                .map(authority -> new SimpleGrantedAuthority((String) authority))
                .collect(Collectors.toList());

            
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
              
        } 
        
        return null;
//        else {
//        	throw new InvalidJsonWebToken();
//        }
    }
}
