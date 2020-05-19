package upcafe.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import upcafe.entity.signin.User;
import upcafe.repository.signin.UserRepository;
import upcafe.security.model.UserPrincipal;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username).orElseThrow(()
        -> new UsernameNotFoundException("User with email { " + username + " } was not found."));

        return UserPrincipal.create(user);
    }

    public UserDetails loadUserById(int id) {
        User user = userRepo.findById(id).orElse(null);

        return UserPrincipal.create(user);
    }
}
