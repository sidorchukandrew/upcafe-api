package upcafe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import upcafe.dto.order.OrderDTO;

@Controller
@Configuration
@EnableScheduling
public class FeedController {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

//	@PreAuthorize("hasRole('STAFF')")
	public void send(OrderDTO order, String state) {
		this.simpMessagingTemplate.convertAndSend("/" + state.toLowerCase(), order);
	}

}
