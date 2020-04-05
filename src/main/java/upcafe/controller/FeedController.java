package upcafe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import upcafe.entity.orders.Orders;

@Controller
public class FeedController {
	
	@Autowired private SimpMessagingTemplate simpMessagingTemplate;

	@MessageMapping("/hello")
	@SendTo("/topic")
	public String connect(String incoming) {
		System.out.println(incoming);
		return "Thanks!";
	}
	
	public void send(Orders order) {
		System.out.println("\n\n\nAttempting to send : " + order + "\n\n\n");
		this.simpMessagingTemplate.convertAndSend("/topic", order);
	}
}
