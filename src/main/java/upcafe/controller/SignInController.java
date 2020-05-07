package upcafe.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import upcafe.dto.users.UserDTO;
import upcafe.service.SignInService;

@RestController
@CrossOrigin(origins = "*")
public class SignInController {

	@Autowired
    private SignInService signInService;
    
    @PostMapping("/signin")
    public boolean attemptSignin(@RequestBody UserDTO user) {
        return signInService.attemptSignIn(user);
    }

    @PostMapping("/signin/create")
    public void createUser(@RequestBody UserDTO user) {
        signInService.createUser(user);
    }

}
