package com.catalog.dtos;

import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;

import com.catalog.entities.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsImpl implements UserDetails  {

	private static final long serialVersionUID = 1814849996632489629L;
	
	private String username;
    private String password;
    private Set<Role> authorities;

	@Override
	public Set<Role> getAuthorities() {
		return this.authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
