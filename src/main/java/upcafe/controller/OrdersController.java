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
import upcafe.error.MissingParameterException;
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
	
	@GetMapping(path = "/orders", params="date")
	public Collection<OrderData> getOrders(@RequestParam(name = "date") String date) {
		return ordersService.getOrdersByDate(date);
	}
	
	@GetMapping(path = "/orders/customer/{id}")
	public Orders getActiveCustomerOrder(@PathVariable(name = "id") int customerId, @RequestParam(name = "state") String state) {
		
		if(state.compareTo("ACTIVE") == 0)
			return ordersService.getActiveCustomerOrder(customerId);
		
		return null;
	}
	
	@PostMapping(path = "/orders/pay")
	public boolean pay(@RequestBody PaymentData payment) { 
		
		if(payment.getNonce() == null)
			throw new MissingParameterException("nonce");
		
		if(payment.getOrderId() == null)
			throw new MissingParameterException("order id");
		
		if(payment.getPrice() == 0)
			throw new MissingParameterException("price");
		
		return ordersService.pay(payment);
	}
	
	@GetMapping(path = "/orders", params="state")
	public Collection<OrderData> getOrdersByState(@RequestParam(name = "state") String state) {
		return ordersService.getOrdersByState(state);
	}
	
	@PostMapping(path = "/orders", params="state")
	public void stateChanged(@RequestParam String state, @RequestBody Orders order) {
		
		if(order.getId() == null) 
			throw new MissingParameterException("order id");
		ordersService.changeState(state, order);
	}

}
