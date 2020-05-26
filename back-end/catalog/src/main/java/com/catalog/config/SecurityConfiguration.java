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
import com.catalog.services.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.cors()
        	.and()
            .csrf()
                .csrfTokenRepository(csrfTokenRepository())
            .and()
            .authorizeRequests()
            	.antMatchers(HttpMethod.GET, "/**").hasAnyRole(Roles.ADMIN, Roles.USER)
            	.antMatchers(HttpMethod.POST, "/**").hasRole(Roles.ADMIN)
            	.antMatchers(HttpMethod.PUT, "/**").hasRole(Roles.ADMIN)
            	.antMatchers(HttpMethod.DELETE, "/**").hasRole(Roles.ADMIN)
            .and()
            .formLogin()
                //.successHandler(this.handler)
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
            .and()
            .rememberMe()
                .rememberMeParameter("remember-me")
                .userDetailsService(this.userService)
                .rememberMeCookieName("remember")
                .tokenValiditySeconds(1200)
            .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .clearAuthentication(true);

    }
    
    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();

        repository.setSessionAttributeName("_csrf");
        repository.setHeaderName("X-CSRF-TOKEN");

        return repository;
    }
}