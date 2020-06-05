package test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import upcafe.entity.signin.User;
import upcafe.repository.signin.UserRepository;
import upcafe.security.model.Role;
import upcafe.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	
	@InjectMocks
	private UserService userService;
	
	@Mock
	private UserRepository userRepository;
	
	@Test
	public void getUserById_ExistingId_UserReturned() {
		LocalDateTime createdAt = LocalDateTime.now();
		final int ID = 1;
		
		when(userRepository.findById(ID)).thenReturn(
				Optional.of(
				new User.Builder("sidorchukandrew@gmail.com")
				.accountCreatedOn(createdAt)
				.id(ID)
				.imageUrl("https://www.google.com")
				.name("Andrew Sidorchuk")
				.provider("google")
				.roles(new HashSet<Role>())
				.build()));
		
		
		assertNotNull(userService.getUserById(1));
		assertNotNull(userService.getUserById(1).getRoles());
		
		assertEquals("Andrew Sidorchuk", userService.getUserById(1).getName());
		assertEquals("sidorchukandrew@gmail.com", userService.getUserById(1).getEmail());
		assertEquals("https://www.google.com", userService.getUserById(1).getImageUrl());
		assertEquals(0, userService.getUserById(1).getRoles().size());
		assertEquals(ID, userService.getUserById(ID).getId());
	}
	
	@Test
	public void getUserById_NonExistentId_NullReturned() {
		
		final int NONEXISTENT_ID = -1;
		
		when(userRepository.findById(NONEXISTENT_ID)).thenReturn(
				Optional.empty());
		
		assertNull(userService.getUserById(NONEXISTENT_ID));
	}
}
