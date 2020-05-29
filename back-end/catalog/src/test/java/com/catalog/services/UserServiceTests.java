package com.catalog.services;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import com.catalog.dtos.UserDetailsImpl;
import com.catalog.entities.Role;
import com.catalog.entities.User;
import com.catalog.repositories.UserRepository;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTests {

	@Mock
	private UserRepository userRepository;
	
	@Mock
	private ModelMapper modelMapper;
	
	@InjectMocks
	private UserService userService;
	
	private final String username = "username";
	
	@Test
	public void testLoadUserByUsername() {
		
		UserDetailsImpl dto = new UserDetailsImpl();
		User user = new User();
		Set<Role> authorities = new HashSet<>();
		authorities.add(new Role());
		user.setAuthorities(authorities);
		
		when(this.userRepository.findByUsername(this.username)).thenReturn(user);
		when(this.modelMapper.map(user, UserDetailsImpl.class)).thenReturn(dto);
		
		UserDetails result = this.userService.loadUserByUsername(this.username);
		
		assertEquals(dto, result);
	}
	
	@Test
	public void testLoadUserByUsername_invalidUsername_throwsException() {
		
		when(this.userRepository.findByUsername(this.username)).thenReturn(null);
		
		assertThatExceptionOfType(UsernameNotFoundException.class).isThrownBy(() -> {
			this.userService.loadUserByUsername(this.username);
		});
	}
	
	@Test
	public void testLoadUserByUsername_noRoles_throwsException() {
		
		User user = new User();
		user.setAuthorities(new HashSet<>());
		
		when(this.userRepository.findByUsername(this.username)).thenReturn(user);
		
		assertThatExceptionOfType(UsernameNotFoundException.class).isThrownBy(() -> {
			this.userService.loadUserByUsername(this.username);
		});
	}
}
