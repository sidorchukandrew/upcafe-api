//package upcafe.service;
//
//import java.sql.Date;
//import java.sql.Time;
//import java.time.LocalTime;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import upcafe.entity.signin.Customer;
//import upcafe.entity.signin.SignInFormData;
//import upcafe.entity.signin.SignInLog;
//import upcafe.entity.signin.Staff;
//import upcafe.repository.signin.CustomerRepository;
//import upcafe.repository.signin.CustomerSignInsRepository;
//import upcafe.repository.signin.StaffRepository;
//
//@Service
//public class SignInService {
//
//	@Autowired
//	private StaffRepository staffRepository;
//	
//	@Autowired
//	private CustomerRepository customerRepository;
//	
//	@Autowired
//	private CustomerSignInsRepository signInLogsRepository;
//	
//	public Optional<Staff> attemptStaffSignIn(SignInFormData data) {
//		return staffRepository.findByUsernameAndPassword(data.getUsername(), data.getPassword());
//	}
//	
//	public void signInCustomer(Customer customer) {
//		Customer dbCustomer = customerRepository.findByEmail(customer.getEmail());
//		
//		if(dbCustomer == null) {
//			customer.setDateAccountCreated(new Date(System.currentTimeMillis()));
//			customerRepository.save(customer);
//		}
//		else {
//			dbCustomer.setFirstName(customer.getFirstName());
//			dbCustomer.setLastName(customer.getLastName());
//			dbCustomer.setPhotoUrl(customer.getPhotoUrl());
//			dbCustomer = customerRepository.save(dbCustomer);
//		}
//		
//		createSignInLog(dbCustomer);
//	}
//	
//	private void createSignInLog(Customer customer) {
//		SignInLog log = new SignInLog();
//		LocalTime now = LocalTime.now();
//		
//		log.setCustomer(customer);
//		log.setDateSignedIn(new Date(System.currentTimeMillis()));
//		log.setTimeSignedIn(Time.valueOf(now));
//		
//		signInLogsRepository.save(log);
//	}
//	
//	public void signOut(Customer customer) {
//		System.out.println("ENTERED SIGN OUT");
////		Customer dbCustomer = customerRepository.findByEmail(customer.getEmail());
////		SignInLog dbLog = signInLogsRepository.findByCustomerId(customer.getId());
////		
////		LocalTime now = LocalTime.now();
////		dbLog.setTimeSignedOut(Time.valueOf(now));
////		dbLog.setDateSignedOut(new Date(System.currentTimeMillis()));
//	}
//}
