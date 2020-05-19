package upcafe.repository.signin;

import org.springframework.data.repository.CrudRepository;

import upcafe.entity.signin.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer>{

    public Optional<User> findByEmail(String email);
}
