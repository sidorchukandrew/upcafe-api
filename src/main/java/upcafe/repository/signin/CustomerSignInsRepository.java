package upcafe.repository.signin;

import org.springframework.data.jpa.repository.JpaRepository;

import upcafe.entity.signin.SignInLog;

public interface CustomerSignInsRepository extends JpaRepository<SignInLog, Integer> {
	
	public SignInLog findByCustomerId(int id);
}
