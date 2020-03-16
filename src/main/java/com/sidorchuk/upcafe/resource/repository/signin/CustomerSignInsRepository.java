package com.sidorchuk.upcafe.resource.repository.signin;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sidorchuk.upcafe.resource.entity.signin.SignInLog;

public interface CustomerSignInsRepository extends JpaRepository<SignInLog, Integer> {
	
	public SignInLog findByCustomerId(int id);
}
