package upcafe.repository.signin;

import org.springframework.data.repository.CrudRepository;

import upcafe.entity.signin.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer>{
	
	public Customer findByEmail(String email);
}
