package upcafe.repository.signin;

import org.springframework.data.repository.CrudRepository;

import upcafe.entity.signin.User;

public interface UserRepository extends CrudRepository<User, String>{

}
