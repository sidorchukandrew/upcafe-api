package upcafe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import upcafe.model.orders.OrderConfirmation;
import upcafe.model.orders.OrderData;
import upcafe.model.orders.PaymentData;
import upcafe.service.OrdersService;

@RestController
@CrossOrigin(origins = "*")
public class OrdersController {
	
	@Autowired private OrdersService ordersService;
	
	@PostMapping(path = "/orders")
	public OrderConfirmation createOrder(@RequestBody OrderData order) {
		
		System.out.println(order);
		return ordersService.createOrder(order);
	}
	
	@GetMapping(path = "/orders")
	public String getOrder() {
		return "OK";
	}
	
	@PostMapping(path = "/orders/pay")
	public void pay(@RequestBody PaymentData payment) { 
		System.out.println(payment);
		ordersService.pay(payment);
	}

}
