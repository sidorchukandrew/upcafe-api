//package upcafe.controller;
//
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import upcafe.entity.signin.Customer;
//import upcafe.entity.signin.SignInFormData;
//import upcafe.entity.signin.Staff;
//import upcafe.error.MissingParameterException;
//import upcafe.service.SignInService;
//
//@RestController
//@CrossOrigin(origins = "*")
//public class SignInController {
//
//	@Autowired
//	private SignInService signInService;
//
//	@PostMapping(path = "/signin/staff")
//	public Optional<Staff> attemptSignIn(@RequestBody SignInFormData signinFormData) {
//		
//		if(signinFormData.getUsername() == null) 
//			throw new MissingParameterException("username");
//		
//		if(signinFormData.getPassword() == null) 
//			throw new MissingParameterException("password");
//		
//		return signInService.attemptStaffSignIn(signinFormData);
//	}
//	
//	@PostMapping(path = "/signin/customer")
//	public void saveCustomer(@RequestBody Customer customer) {
//		
//		if(customer.getEmail() == null)
//			throw new MissingParameterException("email");
//		
//		signInService.signInCustomer(customer);
//	}
//	
//	@GetMapping(path = "/signout/customer")
//	public void signOutCustomer() {
//		signInService.signOut(null);
//	}
//
//}
