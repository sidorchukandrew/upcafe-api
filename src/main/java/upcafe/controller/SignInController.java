package upcafe.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import upcafe.entity.signin.Customer;
import upcafe.entity.signin.SignInFormData;
import upcafe.entity.signin.Staff;
import upcafe.service.SignInService;

@RestController
@CrossOrigin(origins = "*")
public class SignInController {

	@Autowired
	private SignInService signInService;

	@PostMapping(path = "/signin/staff")
	public Optional<Staff> attemptSignIn(@RequestBody SignInFormData signinFormData) {
		return signInService.attemptStaffSignIn(signinFormData);
	}
	
	@PostMapping(path = "/signin/customer")
	public void saveCustomer(@RequestBody Customer customer) {
		signInService.signInCustomer(customer);
	}
	
	@GetMapping(path = "/signout/customer")
	public void signOutCustomer() {
		signInService.signOut(null);
	}

//	<dependency>
//	<groupId>org.springframework.cloud</groupId>
//	<artifactId>spring-cloud-starter-oauth2</artifactId> 
//</dependency>
//<dependency>
//	<groupId>org.springframework.cloud</groupId>
//	<artifactId>spring-cloud-starter-security</artifactId>
//</dependency>
//	<dependencyManagement>
//	<dependencies>
//		<dependency>
//			<groupId>org.springframework.cloud</groupId>
//			<artifactId>spring-cloud-dependencies</artifactId>
//			<version>${spring-cloud.version}</version>
//			<type>pom</type>
//			<scope>import</scope>
//		</dependency>
//	</dependencies>
//</dependencyManagement>
}
