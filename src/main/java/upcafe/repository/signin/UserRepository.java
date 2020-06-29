package upcafe.repository.signin;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import upcafe.entity.signin.User;

public interface UserRepository extends CrudRepository<User, Integer> {

    public Optional<User> findByEmail(String email);
}
