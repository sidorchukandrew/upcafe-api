package upcafe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upcafe.dto.users.UserDTO;
import upcafe.entity.signin.User;
import upcafe.repository.signin.UserRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public UserDTO getUserById(int id) {

        Optional<User> userOptional = userRepo.findById(id);

        if (userOptional.isPresent()) {
            User localUser = userOptional.get();
            
            return new UserDTO.Builder(localUser.getEmail())
                    .name(localUser.getName())
                    .imageUrl(localUser.getImageUrl())
                    .id(localUser.getId())
                    .roles(localUser.getRoles().stream().map(role -> role.getAuthority()).collect(Collectors.toSet()))
                    .build();
        }

        return null;
    }
}
