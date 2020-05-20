package upcafe.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import upcafe.dto.order.OrderDTO;
import upcafe.dto.order.PaymentDTO;
import upcafe.entity.orders.Orders;
import upcafe.service.OrdersService;

@RestController
@CrossOrigin(origins = "*")
public class OrdersController {
	
	@Autowired private OrdersService ordersService;
	
	@PostMapping(path = "/orders")
	@PreAuthorize(value = "hasAnyRole('CUSTOMER')")
	public Orders createOrder(@RequestBody OrderDTO order) {
		return ordersService.createOrder(order);
	}

	@PostMapping(path = "/orders/pay")
	@PreAuthorize(value = "hasAnyRole('CUSTOMER')")
	public boolean pay(@RequestBody PaymentDTO payment) {
		return ordersService.pay(payment);
	}
	
	@GetMapping(path = "/orders", params="date")
	@PreAuthorize(value = "hasAnyRole('ADMIN', 'STAFF')")
	public Collection<OrderDTO> getOrders(@RequestParam(name = "date") String date) {
		return ordersService.getOrdersByDate(date);
	}
	
	@GetMapping(path = "/orders/customer/{id}")
	@PreAuthorize(value = "hasAnyRole('CUSTOMER')")
	public OrderDTO getActiveCustomerOrder(@PathVariable(name = "id") int customerId, @RequestParam(name = "status") String status) {
		
		if(status.compareTo("ACTIVE") == 0)
			return ordersService.getActiveCustomerOrder(customerId);
		
		return null;
	}
	
	// @GetMapping(path = "/orders", params="state")
	// public Collection<OrderData> getOrdersByState(@RequestParam(name = "state") String state) {
	// 	return ordersService.getOrdersByState(state);
	// }
	
	@PutMapping(path = "/orders", params="status")
	@PreAuthorize(value = "hasAnyRole('STAFF', 'ADMIN')")
	public void statusChanged(@RequestParam(name = "status") String newStatus, @RequestBody OrderDTO order) {
		ordersService.changeStatus(newStatus, order);
	}

}
