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

import upcafe.dto.order.OrderDTO;
import upcafe.dto.order.PaymentDTO;
import upcafe.entity.orders.Orders;
import upcafe.service.OrdersService;

@RestController
@CrossOrigin(origins = "*")
public class OrdersController {
	
	@Autowired private OrdersService ordersService;
	
	@PostMapping(path = "/orders")
	public Orders createOrder(@RequestBody OrderDTO order) {
		return ordersService.createOrder(order);
	}

	@PostMapping(path = "/orders/pay")
	public boolean pay(@RequestBody PaymentDTO payment) {
		return ordersService.pay(payment);
	}
	
	@GetMapping(path = "/orders", params="date")
	public Collection<OrderDTO> getOrders(@RequestParam(name = "date") String date) {
		return ordersService.getOrdersByDate(date);
	}
	
	@GetMapping(path = "/orders/customer/{id}")
	public OrderDTO getActiveCustomerOrder(@PathVariable(name = "id") int customerId, @RequestParam(name = "status") String status) {
		
		if(status.compareTo("ACTIVE") == 0)
			return ordersService.getActiveCustomerOrder(customerId);
		
		return null;
	}
	
	// @GetMapping(path = "/orders", params="state")
	// public Collection<OrderData> getOrdersByState(@RequestParam(name = "state") String state) {
	// 	return ordersService.getOrdersByState(state);
	// }
	
	@PostMapping(path = "/orders", params="status")
	public void statusChanged(@RequestParam(name = "status") String newStatus, @RequestBody OrderDTO order) {
		ordersService.changeStatus(newStatus, order);
	}

}
