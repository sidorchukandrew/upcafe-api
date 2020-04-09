package upcafe.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import upcafe.entity.orders.Orders;
import upcafe.model.orders.OrderData;
import upcafe.model.orders.PaymentData;
import upcafe.service.OrdersService;

@RestController
@CrossOrigin(origins = "*")
public class OrdersController {
	
	@Autowired private OrdersService ordersService;
	
	@PostMapping(path = "/orders")
	public Orders createOrder(@RequestBody OrderData order) {
		
		System.out.println(order);
		
		return ordersService.createOrder(order);
	}
	
	@GetMapping(path = "/orders")
	public String getOrder() {
		return "OK";
	}
	
	@GetMapping(path = "/orders/customer/{id}")
	public List<Orders> getOpenCustomerOrders(@PathVariable(name = "id") int customerId) {
		
		return ordersService.getOpenOrdersByCustomerId(customerId);
	}
	
	@PostMapping(path = "/orders/pay")
	public boolean pay(@RequestBody PaymentData payment) { 
		
		return ordersService.pay(payment);
	}
	
	@GetMapping(path = "/orders", params="state")
	public Collection<OrderData> getOrdersByState(@RequestParam(name = "state") String state) {
		return ordersService.getOrdersByState(state);
	}
	
	@PostMapping(path = "/orders", params="state")
	public void stateChanged(@RequestParam String state, @RequestBody Orders order) {
		
		ordersService.changeState(state, order);
	}

}
