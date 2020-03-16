package com.sidorchuk.upcafe.resource.repository.signin;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.sidorchuk.upcafe.resource.entity.signin.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer>{
	
	public Customer findByEmail(String email);
}
