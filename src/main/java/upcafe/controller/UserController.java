package upcafe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import upcafe.annotation.CurrentUser;
import upcafe.dto.users.UserDTO;
import upcafe.entity.signin.User;
import upcafe.security.model.UserPrincipal;
import upcafe.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/users/me")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'STAFF', 'ADMIN')")
    public UserDTO getCurrentlyAuthenticatedUser(@CurrentUser UserPrincipal userPrincipal) {
        return userService.getUserById(userPrincipal.getId());
    }
    
    @GetMapping(path = "/api/v1/users")
    @PreAuthorize("hasRole('ADMIN')")
    public Iterable<User> getAllUsersForAdminView() {
    	return userService.getUserEntities();
    }
}
