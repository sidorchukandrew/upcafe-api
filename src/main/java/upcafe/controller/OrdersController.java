package upcafe.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import upcafe.model.catalog.VariationData;
import upcafe.model.orders.OrderData;
import upcafe.model.orders.SelectedItem;
import upcafe.service.OrdersService;

@RestController
@CrossOrigin(origins = "*")
public class OrdersController {
	
	@Autowired private OrdersService ordersService;
	
	@PostMapping(path = "/orders")
	public String createOrder() {
		
//		System.out.println(order);
		
//		OrderData o = new OrderData();
//		List<SelectedItem> selectedItems = new ArrayList<SelectedItem>();
//		VariationData varData = new VariationData();
//		varData.setVariationId("SS6TFUQ4DMMXCH3U65H4IAVL");
//		selectedItems.add(new SelectedItem(1, varData,2.0, null));
//		o.setSelectedLineItems(selectedItems);
//		ordersService.createOrder(o);
		ordersService.pay();
		return "Ok";
	}
	
	@GetMapping(path = "/orders")
	public String getOrder() {
		return "OK";
	}

}
