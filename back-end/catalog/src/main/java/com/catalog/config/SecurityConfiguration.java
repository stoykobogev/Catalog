package com.catalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import com.catalog.constants.Roles;
import com.catalog.constants.SecurityConstants;
import com.catalog.filters.JwtAuthenticationFilter;
import com.catalog.filters.JwtAuthorizationFilter;
import com.catalog.handlers.JwtLogoutSuccessHandler;
import com.catalog.services.RedisService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private RedisService redisService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.cors()
        	.and()
            .csrf()
            	.ignoringAntMatchers(SecurityConstants.LOGIN_URL)
                .csrfTokenRepository(csrfTokenRepository())
            .and()
            .authorizeRequests()
            	.antMatchers(HttpMethod.POST, "/api/login").permitAll()
            	.antMatchers(HttpMethod.GET, "/api/**").hasAnyRole(Roles.ADMIN, Roles.USER)
            	.antMatchers(HttpMethod.POST, "/api/**").hasRole(Roles.ADMIN)
            	.antMatchers(HttpMethod.PUT, "/api/**").hasRole(Roles.ADMIN)
            	.antMatchers(HttpMethod.DELETE, "/api/**").hasRole(Roles.ADMIN)
            	.anyRequest().authenticated()
            .and()
            .logout()
            	.logoutUrl(SecurityConstants.LOGOUT_URL)
            	.deleteCookies(SecurityConstants.JWT_COOKIE_NAME, "JSESSIONID")
            	.logoutSuccessHandler(new JwtLogoutSuccessHandler(this.redisService))
            .and()
            .addFilter(new JwtAuthenticationFilter(authenticationManager(), this.redisService, this.objectMapper))
            .addFilter(new JwtAuthorizationFilter(authenticationManager(), this.redisService));

    }
    
    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();

        return repository;
    }
}
