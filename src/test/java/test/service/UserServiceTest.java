package test.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import upcafe.entity.orders.Orders;
import upcafe.entity.signin.User;
import upcafe.error.NonExistentIdFoundException;
import upcafe.repository.orders.OrderRepository;
import upcafe.repository.orders.PaymentRepository;
import upcafe.repository.signin.UserRepository;
import upcafe.security.model.Role;
import upcafe.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	
	@InjectMocks
	private UserService userService;
	
	@Mock
	private UserRepository userRepository;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private PaymentRepository paymentRepository;

	@Test
	public void deleteUserById_UserHasNotPlacedAnyOrdersYet_Successful() {
		assertTrue(userService.deleteUserById(1));
		verify(paymentRepository, times(0)).deleteByOrderId(anyString());
		verify(orderRepository, times(0)).deleteById(anyString());
		verify(userRepository, times(1)).deleteById(1);
	}

	@Test
	public void deleteUserById_UserHasPlacedOrdersBefore_Successful() {
		when(orderRepository.getByCustomerId(1)).thenReturn(Arrays.asList(new Orders.Builder("SH929J23KD020J23R").build()));

		assertTrue(userService.deleteUserById(1));
		verify(paymentRepository).deleteByOrderId(anyString());
		verify(orderRepository).deleteById(anyString());
		verify(userRepository, times(1)).deleteById(1);
	}

	@Test
	public void deleteUserById_NonPositiveIdGiven_ExceptionThrown() {
		assertThrows(NonExistentIdFoundException.class, () -> userService.deleteUserById(-1));
		assertThrows(NonExistentIdFoundException.class, () -> userService.deleteUserById(0));
	}

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
