package com.sidorchuk.upcafe.resource.repository.signin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sidorchuk.upcafe.resource.entity.signin.Staff;

public interface StaffRepository extends JpaRepository<Staff, Integer> {

	public Optional<Staff> findByUsernameAndPassword(String username, String password);
}
