package upcafe.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upcafe.dto.users.UserDTO;
import upcafe.entity.signin.User;
import upcafe.repository.signin.UserRepository;

@Service
public class SignInService {

    @Autowired
    private UserRepository userRepository;

    public boolean attemptSignIn(UserDTO userDTO) {
        Optional<User> userOpt = userRepository.findById(userDTO.getEmail());

        return userOpt.isPresent();
    }

    public void createUser(UserDTO userDTO) {
        userRepository.save(new User.Builder(userDTO.getEmail())
                                .accountCreatedOn(LocalDateTime.now())
                                .firstName(userDTO.getFirstName())
                                .lastName(userDTO.getLastName())
                                .isAdmin(false)
                                .isStaff(false)
                                .photoUrl(userDTO.getPhotoUrl())
                                .build());
    }

}
