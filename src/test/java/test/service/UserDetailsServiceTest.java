package test.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import upcafe.entity.signin.User;
import upcafe.error.MissingParameterException;
import upcafe.error.NonExistentIdFoundException;
import upcafe.repository.signin.UserRepository;
import upcafe.security.model.Role;
import upcafe.security.service.UserDetailsService;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {

	@InjectMocks
	private UserDetailsService userDetailsService;
	
	@Mock
	private UserRepository userRepo;
	
	
	@Test
	public void loadUserById_ExistingIdPassed_Successful() {
		User user = new User.Builder("sidorchukandrew@gmail.com")
				.accountCreatedOn(LocalDateTime.of(2020, 4, 10, 10, 40))
				.id(8)
				.imageUrl("https://www.google.com")
				.name("Andrew Sidorchuk")
				.provider("facebook")
				.roles(new HashSet<>(Arrays.asList(new Role.Builder("ROLE_CUSTOMER").build())))
				.build();
		
		when(userRepo.findById(8)).thenReturn(Optional.of(user));
		
		UserDetails userDetails = userDetailsService.loadUserById(8);
		assertNotNull(userDetails);
		assertEquals(1, userDetails.getAuthorities().size());
		assertEquals("sidorchukandrew@gmail.com", userDetails.getUsername());
		assertNull(userDetails.getPassword());
	}
	
	@Test
	public void loadUserById_NonExistentIdPassed_ExceptionThrown() {
		when(userRepo.findById(10)).thenReturn(Optional.empty());
		
		assertThrows(NonExistentIdFoundException.class, () -> userDetailsService.loadUserById(10));
	}
	
	@Test
	public void loadUserByUsername_ValidUsernamePassed_Successful() {
		User user = new User.Builder("sidorchukandrew@gmail.com")
				.accountCreatedOn(LocalDateTime.of(2020, 4, 10, 10, 40))
				.id(8)
				.imageUrl("https://www.google.com")
				.name("Andrew Sidorchuk")
				.provider("facebook")
				.roles(new HashSet<>(Arrays.asList(new Role.Builder("ROLE_CUSTOMER").build())))
				.build();
		
		
		when(userRepo.findByEmail("sidorchukandrew@gmail.com")).thenReturn(Optional.of(user));
		
		UserDetails userDetails = userDetailsService.loadUserByUsername("sidorchukandrew@gmail.com");
		
		assertNotNull(userDetails);
		assertEquals(1, userDetails.getAuthorities().size());
		assertEquals("sidorchukandrew@gmail.com", userDetails.getUsername());
		assertNull(userDetails.getPassword());
	}
	
	@Test
	public void loadUserByUsername_UsernameNotFound_ExceptionThrown() {
		when(userRepo.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("sidorchukandrew"));
	}
	
	@Test
	public void loadUserByUsername_NoUsernamePassed_ExceptionThrown() {
		assertThrows(MissingParameterException.class, () -> userDetailsService.loadUserByUsername(""));
	}
	
	@Test
	public void loadUserByUsername_NullPassed_ExceptionThrown() {
		assertThrows(MissingParameterException.class, () -> userDetailsService.loadUserByUsername(null));
	}
}
