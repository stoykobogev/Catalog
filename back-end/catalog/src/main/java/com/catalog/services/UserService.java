package com.catalog.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.catalog.dtos.UserDetailsImpl;
import com.catalog.entities.User;
import com.catalog.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = this.userRepository.findByUsername(username);
		
		if (user == null) {
			throw new UsernameNotFoundException(String.format("Username '%s' was not found", username));
		} else if (user.getAuthorities().isEmpty()) {
			throw new UsernameNotFoundException(String.format("User '%s' has no authorities", username));
		}
		
		UserDetailsImpl dto = this.modelMapper.map(user, UserDetailsImpl.class);
		
		return dto;
	}

}
