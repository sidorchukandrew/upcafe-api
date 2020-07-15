package test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import upcafe.dto.feedback.BugDTO;
import upcafe.dto.feedback.FeatureDTO;
import upcafe.dto.users.UserDTO;
import upcafe.entity.feedback.Bug;
import upcafe.entity.feedback.FeatureRequest;
import upcafe.entity.signin.User;
import upcafe.error.MissingParameterException;
import upcafe.error.NonExistentIdFoundException;
import upcafe.repository.feedback.BugReportRepository;
import upcafe.repository.feedback.FeatureRequestRepository;
import upcafe.service.FeedbackService;

@ExtendWith(MockitoExtension.class)
public class FeedbackServiceTest {
	
	@InjectMocks
	private FeedbackService feedbackService;
	
	@Mock
	private FeatureRequestRepository featureRepo;
	
	@Mock
	private BugReportRepository bugRepo;
	

	@Test
	public void saveNewBug_MissingParameterActual_ExceptionThrown() {
		assertThrows(MissingParameterException.class, () -> feedbackService.saveBugReport(new BugDTO.Builder().build()));
	}
	
	@Test
	public void saveNewBug_DetailsPresent_Success() {
		final LocalDateTime DATE_REPORTED = LocalDateTime.now();
		final UserDTO REPORTER = new UserDTO.Builder("sidorchukandrew@gmail.com")
				.id(8)
				.imageUrl("https://www.google.com")
				.name("Andrew Sidorchuk")
				.roles(new HashSet<String>())
				.build();
		
		BugDTO reportedBug = new BugDTO.Builder()
				.browser("Mozilla")
				.dateReported(DATE_REPORTED)
				.expectation("The image should've been square")
				.actual("The image looked rectangular")
				.platform("iOS")
				.page("Order Item Page")
				.reporter(REPORTER)
				.build();
		
		when(bugRepo.save(Mockito.any(Bug.class))).thenReturn(new Bug.Builder()
				.actual(reportedBug.getActual())
				.expectation(reportedBug.getExpectation())
				.browser(reportedBug.getBrowser())
				.dateReported(DATE_REPORTED)
				.extraInformation(reportedBug.getExtraInformation())
				.id(1)
				.page(reportedBug.getPage())
				.platform(reportedBug.getPlatform())
				.reporter(new User.Builder(REPORTER.getEmail()).id(REPORTER.getId())
						.name(REPORTER.getName())
						.imageUrl(REPORTER.getImageUrl())
						.build())
				.build());
		
		BugDTO returnedBug = feedbackService.saveBugReport(reportedBug);
		
		
		assertNotNull(returnedBug);
		
		// Check if bug data was transferred correctly
		
		assertEquals(reportedBug.getActual(), returnedBug.getActual());
		assertEquals(reportedBug.getDateReported(), returnedBug.getDateReported());
		assertEquals(reportedBug.getExpectation(), returnedBug.getExpectation());
		assertEquals(reportedBug.getBrowser(), returnedBug.getBrowser());
		assertEquals(reportedBug.getExtraInformation(), returnedBug.getExtraInformation());
		assertEquals(reportedBug.getPage(), returnedBug.getPage());
		assertEquals(reportedBug.getPlatform(), returnedBug.getPlatform());
		
		//Confirm the database assigned an ID
		assertEquals(1, returnedBug.getId());
		
		// Check if reporter data was transferred correctly
		
		assertEquals(REPORTER.getId(), returnedBug.getReporter().getId());
		assertEquals(REPORTER.getEmail(), returnedBug.getReporter().getEmail());
		assertEquals(REPORTER.getName(), returnedBug.getReporter().getName());
		assertEquals(REPORTER.getImageUrl(), returnedBug.getReporter().getImageUrl());
	}
	
	@Test
	public void saveNewBug_NullPassed__ExceptionThrown() {
		assertThrows(NonExistentIdFoundException.class, () -> feedbackService.saveBugReport(null));
	}
	
	@Test
	public void saveNewFeatureRequest_NullPassed_ExceptionThrown() {
		assertThrows(NonExistentIdFoundException.class, () -> feedbackService.saveFeatureRequest(null));
	}
	
	@Test
	public void saveNewFeatureRequest_MissingParameterActual_ExceptionThrown() {
		assertThrows(MissingParameterException.class, () -> feedbackService.saveFeatureRequest(new FeatureDTO.Builder().build()));
	}
	
	@Test
	public void saveNewFeatureRequest_AllParametersPresent_Succesful() {
		final LocalDateTime DATE_REPORTED = LocalDateTime.now();
		final UserDTO REPORTER = new UserDTO.Builder("sidorchukandrew@gmail.com")
				.id(8)
				.imageUrl("https://www.google.com")
				.name("Andrew Sidorchuk")
				.roles(new HashSet<String>())
				.build();
		
		FeatureDTO feature = new FeatureDTO.Builder()
				.description("Pay with Google Pay")
				.dateReported(DATE_REPORTED)
				.page("Payment page")
				.reporter(REPORTER)
				.build();
		
		when(featureRepo.save(Mockito.any(FeatureRequest.class))).thenReturn(new FeatureRequest.Builder()
				.dateReported(DATE_REPORTED)
				.description("Pay with Google Pay")
				.id(1)
				.page("Payment page")
				.reporter(new User.Builder("sidorchukandrew@gmail.com").id(8)
						.name("Andrew Sidorchuk")
						.imageUrl("https://www.google.com")
						.build())
				.build());
		
		FeatureDTO saved = feedbackService.saveFeatureRequest(feature);
		
		assertNotNull(saved);
		
		
		//Check if feature request was saved correctly
		assertEquals(feature.getDateReported(), saved.getDateReported());
		assertEquals(feature.getDescription(), saved.getDescription());
		assertEquals(feature.getPage(), saved.getPage());
		
		//Check if database created an ID for the request
		assertEquals(1, saved.getId());
		
		//Check if the reporter data was transferred correctly
		assertEquals(REPORTER.getId(), saved.getReporter().getId());
		assertEquals(REPORTER.getEmail(), saved.getReporter().getEmail());
		assertEquals(REPORTER.getName(), saved.getReporter().getName());
		assertEquals(REPORTER.getImageUrl(), saved.getReporter().getImageUrl());
	}
}
