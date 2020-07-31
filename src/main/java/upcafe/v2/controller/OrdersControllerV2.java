package upcafe.v2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import upcafe.v2.models.Order;
import upcafe.v2.service.OrdersServiceV2;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v2/orders")
public class OrdersControllerV2 {

    @Autowired
    private OrdersServiceV2 ordersService;

    @GetMapping
    public List<com.squareup.square.models.Order> getAllOrdersForLocation() {
        return ordersService.getOrdersForLocation();
    }

    @GetMapping(params = "date")
    public List<com.squareup.square.models.Order> getOrdersForDate(@RequestParam("date") String date) {
        return ordersService.getOrdersForDate(date);
    }

    @GetMapping(path = "/{orderId}")
    public Order getOrderById(@PathVariable("orderId") String orderId) {
        return ordersService.getOrderById(orderId);
    }

    @PutMapping(path = "/{orderId}")
    public void updateOrderFulfillmentState(@PathVariable(name = "orderId") String orderId,
                                                @RequestParam(name = "fulfillmentId") String fulfillmentId,
                                                @RequestParam(name = "state") String state) {

        int version = ordersService.getMostRecentVersionByOrderId(orderId);
        ordersService.updateOrderFulfillmentState(orderId, fulfillmentId, state, version);

    }

    @PostMapping
    public void createNewOrder(@RequestBody Order order) {
        ordersService.createOrder(order);
    }

}
