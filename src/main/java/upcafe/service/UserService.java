package upcafe.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upcafe.dto.users.UserDTO;
import upcafe.entity.orders.Orders;
import upcafe.entity.signin.User;
import upcafe.error.NonExistentIdFoundException;
import upcafe.repository.orders.OrderRepository;
import upcafe.repository.orders.PaymentRepository;
import upcafe.repository.signin.UserRepository;
import upcafe.security.model.Role;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private PaymentRepository paymentRepo;

	public UserDTO getUserById(int id) {

		Optional<User> userOptional = userRepo.findById(id);

		if (userOptional.isPresent()) {
			User localUser = userOptional.get();

			return new UserDTO.Builder(localUser.getEmail()).name(localUser.getName()).imageUrl(localUser.getImageUrl())
					.id(localUser.getId())
					.roles(localUser.getRoles().stream().map(role -> role.getAuthority()).collect(Collectors.toSet()))
					.build();
		}

		return null;
	}

	public Iterable<User> getUserEntities() {
		return userRepo.findAll();
	}

	public User updateUser(User updatedUser) {
		Optional<User> userOpt = userRepo.findById(updatedUser.getId());

		if (userOpt.isPresent()) {
			User user = userOpt.get();

			return userRepo.save(new User.Builder(updatedUser.getEmail())
					.accountCreatedOn(user.getAccountCreatedOn()).id(user.getId())
					.imageUrl(updatedUser.getImageUrl()).name(updatedUser.getName()).roles(updatedUser.getRoles())
					.provider(user.getProvider()).build());
		} else {
			throw new NonExistentIdFoundException(updatedUser.getId() + "", "User");
		}
	}

	public boolean deleteUserById(int id) {
		if(id <= 0) throw new NonExistentIdFoundException(id + "", "User");

		List<Orders> usersOrders = orderRepo.getByCustomerId(id);

		usersOrders.forEach(order -> {
			paymentRepo.deleteByOrderId(order.getId());
			orderRepo.deleteById(order.getId());
		});

		userRepo.deleteById(id);

		return true;
	}

	private Role toRole(String roleName) {
		if (roleName.compareTo("ROLE_ADMIN") == 0) {
			return new Role.Builder(roleName).setId(3).build();
		} else if (roleName.compareTo("ROLE_STAFF") == 0) {
			return new Role.Builder(roleName).setId(2).build();
		} else if (roleName.compareTo("ROLE_CUSTOMER") == 0) {
			return new Role.Builder(roleName).setId(1).build();
		} else
			throw new NonExistentIdFoundException(roleName, "Role");
	}
}
