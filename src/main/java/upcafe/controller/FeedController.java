package upcafe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;

import upcafe.dto.order.OrderDTO;
import upcafe.entity.orders.Orders;

@Controller
@Configuration
@EnableScheduling
public class FeedController {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	public void send(OrderDTO order, String state) {
		this.simpMessagingTemplate.convertAndSend("/" + state.toLowerCase(), order);
	}

}
