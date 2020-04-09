package upcafe.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import upcafe.entity.orders.Orders;

@Controller
@Configuration
@EnableScheduling
public class FeedController {
	
	@Autowired private SimpMessagingTemplate simpMessagingTemplate;
	
	public void send(Orders order, String state) {
		this.simpMessagingTemplate.convertAndSend("/" + state, order);
	}

}
