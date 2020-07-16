package upcafe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import upcafe.annotation.CurrentUser;
import upcafe.dto.users.UserDTO;
import upcafe.entity.signin.User;
import upcafe.security.model.UserPrincipal;
import upcafe.service.UserService;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/me")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'STAFF', 'ADMIN')")
    public UserDTO getCurrentlyAuthenticatedUser(@CurrentUser UserPrincipal userPrincipal) {
        return userService.getUserById(userPrincipal.getId());
    }

    @DeleteMapping(path = "/me")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'STAFF', 'ADMIN')")
    public boolean deleteCurrentlyAuthenticatedUser(@CurrentUser UserPrincipal userPrincipal) {
        return userService.deleteUserById(userPrincipal.getId());
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Iterable<User> getAllUsersForAdminView() {
    	return userService.getUserEntities();
    }
    
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public User updateUser(@RequestBody User updatedUser) {
    	System.out.println(updatedUser);
    	return userService.updateUser(updatedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public boolean deleteUser(@PathVariable(name = "id") int id) {
        userService.deleteUserById(id);
        return true;
    }
}
