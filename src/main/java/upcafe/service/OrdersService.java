package upcafe.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.squareup.square.SquareClient;
import com.squareup.square.exceptions.ApiException;
import com.squareup.square.models.BatchRetrieveOrdersRequest;
import com.squareup.square.models.BatchRetrieveOrdersResponse;
import com.squareup.square.models.CreateOrderRequest;
import com.squareup.square.models.CreateOrderResponse;
import com.squareup.square.models.CreatePaymentRequest;
import com.squareup.square.models.CreatePaymentResponse;
import com.squareup.square.models.Money;
import com.squareup.square.models.Order;
import com.squareup.square.models.OrderLineItem;
import com.squareup.square.models.OrderLineItemModifier;

import upcafe.controller.FeedController;
import upcafe.entity.orders.Orders;
import upcafe.entity.signin.Customer;
import upcafe.model.catalog.ModifierData;
import upcafe.model.catalog.VariationData;
import upcafe.model.orders.OrderData;
import upcafe.model.orders.PaymentData;
import upcafe.model.orders.SelectedItem;
import upcafe.repository.orders.OrderRepository;
import upcafe.repository.orders.PaymentRepository;

@Service
public class OrdersService {

	@Autowired private SquareClient client;
	@Autowired private OrderRepository orderRepository;
	@Autowired private PaymentRepository paymentRepository;
	@Autowired private FeedController feed;
	
	
	public List<Orders> getOrdersByDate(String date) {
		return orderRepository.getOrdersByPickupDate(date);
	}
	
	
	public Orders createOrder(OrderData orderData) {
		
		List<OrderLineItem> orderLineItems = new ArrayList<OrderLineItem>();
		
		orderData.getSelectedLineItems().forEach(selectedItem -> {
			List<OrderLineItemModifier> orderLineItemModifiers = new ArrayList<OrderLineItemModifier>();
			
			// Add all the modifiers
			if(selectedItem.getSelectedModifiers() != null) {
				selectedItem.getSelectedModifiers().forEach(modifier -> {
					OrderLineItemModifier orderLineItemModifier = new OrderLineItemModifier.Builder()
							.catalogObjectId(modifier.getId())
							.build();
					
					orderLineItemModifiers.add(orderLineItemModifier);
				});
			}
			
			// Add all the variation data
			OrderLineItem orderLineItem = new OrderLineItem.Builder(selectedItem.getQuantity() + "")
					.catalogObjectId(selectedItem.getVariationData().getVariationId())
					.modifiers(orderLineItemModifiers)
					.build();
			
			orderLineItems.add(orderLineItem);
		});
		
		
		Order order = new Order.Builder(System.getenv("SQUARE_LOCATION"))
				.lineItems(orderLineItems)
				.build();
		
		
		CreateOrderRequest body = new CreateOrderRequest.Builder()
				.idempotencyKey(UUID.randomUUID().toString())
				.order(order)
				.build();
		
		try {
			CreateOrderResponse response = client.getOrdersApi().createOrder(System.getenv("SQUARE_LOCATION"), body);
			upcafe.entity.orders.Orders dbOrder = new upcafe.entity.orders.Orders();
			
			dbOrder.setCreatedAt(response.getOrder().getCreatedAt());
			dbOrder.setId(response.getOrder().getId());
			dbOrder.setState("ORDER PLACED");
			dbOrder.setTotalPrice((double)response.getOrder().getTotalMoney().getAmount() / 100);
			dbOrder.setPickupTime(orderData.getPickupTime());
		
			dbOrder.setCustomer(orderData.getCustomer());
			
			Orders confirmation = orderRepository.save(dbOrder);
			confirmation.setTotalPrice((double)response.getOrder().getTotalMoney().getAmount() / 100);
			
			feed.send(confirmation, "new");
			return confirmation;
			
		} catch (ApiException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean pay(PaymentData payment) {
		
		Money amountMoney = new Money.Builder()
				.currency("USD")
				.amount((long)(payment.getPrice() * 100))
				.build();
		
		CreatePaymentRequest body = new CreatePaymentRequest.Builder(payment.getNonce(), UUID.randomUUID().toString(), amountMoney)
				.autocomplete(true)
				.locationId(System.getenv("SQUARE_LOCATION"))
				.orderId(payment.getOrderId())
				.build();
		
		try {
			CreatePaymentResponse response = client.getPaymentsApi().createPayment(body);
			
			upcafe.entity.orders.Payment paymentConfirmation = new upcafe.entity.orders.Payment();
			
			paymentConfirmation.setTotalPaid((double)response.getPayment().getAmountMoney().getAmount() / 100);
			paymentConfirmation.setId(response.getPayment().getId());
			paymentConfirmation.setReceiptUrl(response.getPayment().getReceiptUrl());
			paymentConfirmation.setPaymentMadeAt(response.getPayment().getCreatedAt());
			paymentConfirmation.setStatus(response.getPayment().getStatus());
			
			Customer tempCustomer = new Customer();
			
			// NEEDS TO BE CHANGED LATER
			tempCustomer.setId(5);
			
			paymentConfirmation.setCustomer(tempCustomer);
			
			Optional<Orders> tempOrder = orderRepository.findById(response.getPayment().getOrderId());
			paymentConfirmation.setOrder(tempOrder.get());
			
			paymentRepository.save(paymentConfirmation);
						
			return true;
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public List<Orders> getOpenOrdersByCustomerId(int id) {
		return orderRepository.getOpenOrdersByCustomerId(id);
	}
	
	public Collection<OrderData> getOrdersByState(String state) {
		
		// Get the IDs of the orders with that state from the local repository
		
		Hashtable<String, OrderData> orders = new Hashtable<String, OrderData>();
		List<String> orderIdsToRetrieve = new ArrayList<String>();
		
		orderRepository.getOrdersByState(state).forEach(order -> {
			OrderData data = new OrderData();
			data.setCreatedAt(order.getCreatedAt());
			data.setCustomer(order.getCustomer());
			data.setPickupTime(order.getPickupTime());
			data.setState(order.getState());
			data.setTotalPrice(order.getTotalPrice());
			data.setId(order.getId());
			
			orders.put(data.getId(), data);
			orderIdsToRetrieve.add(data.getId());
		});
		
		
		if (orderIdsToRetrieve.size() > 0) {
			
			// Get the actual items for each order in Square
			BatchRetrieveOrdersRequest request = new BatchRetrieveOrdersRequest.Builder(orderIdsToRetrieve).build();

			try {

				// Collect order information from Square

				BatchRetrieveOrdersResponse response = client.getOrdersApi()
						.batchRetrieveOrders(System.getenv("SQUARE_LOCATION"), request);

				// For each returned order, collect the item information
				response.getOrders().forEach(order -> {

					List<SelectedItem> items = new ArrayList<SelectedItem>();

					// For each item in the order collect its information
					order.getLineItems().forEach(squareItem -> {

						SelectedItem item = new SelectedItem();
						item.setQuantity(Integer.parseInt(squareItem.getQuantity()));
						item.setPrice((double) squareItem.getGrossSalesMoney().getAmount() / 100);

						VariationData variation = new VariationData();

						if (squareItem.getVariationName().compareTo("Regular") == 0)
							variation.setName(squareItem.getName());
						else
							variation.setName(squareItem.getVariationName());

						variation
								.setVariationPrice((double) squareItem.getVariationTotalPriceMoney().getAmount() / 100);
						variation.setVariationId(squareItem.getCatalogObjectId());

						item.setVariationData(variation);

						ArrayList<ModifierData> modifiers = new ArrayList<ModifierData>();

						if (squareItem.getModifiers() != null) {
							// For each modifier corresponding to this item, collect the modifier
							// information
							squareItem.getModifiers().forEach(squareModifier -> {
								ModifierData modifier = new ModifierData();
								modifier.setId(squareModifier.getCatalogObjectId());
								modifier.setName(squareModifier.getName());
								modifier.setPrice((double) squareModifier.getTotalPriceMoney().getAmount() / 100);

								modifiers.add(modifier);
							}); // end forEach modifier in item

							item.setSelectedModifiers(modifiers);
							items.add(item);
						}

					}); // end forEach item in Square order

					// Add the line items to the order
					String orderId = order.getId();

					if (orders.containsKey(orderId))
						orders.get(orderId).setSelectedLineItems(items);
					else
						System.out.println("Something went seriously wrong.");

				}); // end forEach Square order

				orders.values().forEach(order -> {
					System.out.println(order);
				});

				return orders.values();

			} catch (ApiException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	public void changeState(String state, Orders order) {
		
		if(state.compareTo("active") == 0) {
			System.out.println("\t\t\t\tSAVING - - - - - - - - - - - - - - - -  -\n" + order);
			order.setState(state.toUpperCase());
		}
		
		orderRepository.save(order);
		feed.send(order, state);
	}
}
