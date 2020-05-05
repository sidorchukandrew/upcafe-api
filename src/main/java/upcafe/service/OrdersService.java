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
import com.squareup.square.models.CreatePaymentRequest;
import com.squareup.square.models.Money;
import com.squareup.square.models.Order;
import com.squareup.square.models.OrderLineItem;
import com.squareup.square.models.OrderLineItemModifier;
import com.squareup.square.models.Payment;

import upcafe.controller.FeedController;
import upcafe.dto.order.OrderDTO;
import upcafe.dto.order.OrderItemDTO;
import upcafe.dto.order.OrderModifierDTO;
import upcafe.dto.order.PaymentDTO;
import upcafe.entity.orders.Orders;
import upcafe.entity.signin.Customer;
import upcafe.error.MissingParameterException;
import upcafe.repository.orders.OrderRepository;
import upcafe.repository.orders.PaymentRepository;

@Service
public class OrdersService {

	private static final int SMALLEST_CURRENCY_DENOMINATOR = 100;

	@Autowired
	private SquareClient client;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private PaymentRepository paymentRepository;
	@Autowired
	private FeedController feed;

	// public Collection<OrderData> getOrdersByDate(String date) {
		
	// 	Hashtable<String, OrderData> orders = new Hashtable<String, OrderData>();
	// 	List<String> orderIdsToRetrieve = new ArrayList<String>();
		
	// 	 orderRepository.getOrdersByPickupDate(date).forEach(order -> {
	// 			OrderData data = new OrderData();
	// 			data.setCreatedAt(order.getCreatedAt());
	// 			data.setCustomer(order.getCustomer());
	// 			data.setPickupTime(order.getPickupTime());
	// 			data.setPickupDate(order.getPickupDate());
	// 			data.setState(order.getState());
	// 			data.setTotalPrice(order.getTotalPrice());
	// 			data.setId(order.getId());

	// 			orders.put(data.getId(), data);
	// 			orderIdsToRetrieve.add(data.getId());
	// 		});
		 
	// 	 return getOrdersByIdFromSquare(orderIdsToRetrieve, orders);
	// }

	public Orders createOrder(OrderDTO orderDTO) {

		checkParametersForOrder(orderDTO);

		Order orderSquare = saveOrderInSquare(orderDTO);
		return saveOrderLocally(orderSquare, orderDTO);
	}

	private void checkParametersForOrder(OrderDTO order) {

		StringBuilder missingParameters = new StringBuilder("");

		if(order.getCustomer() == null)
			missingParameters.append("customer id, ");
		
		if(order.getTotalPrice() <= 0)
			missingParameters.append("total price, ");

		if(order.getOrderItems() == null) {
			missingParameters.append("order items");
		}
		else {
			order.getOrderItems().forEach(item -> {

			if(item.getVariationId() == null || item.getVariationId().compareTo("") == 0)
				missingParameters.append("variation id");
			});
		}

		if(missingParameters.length() > 0)
			throw new MissingParameterException(missingParameters.toString());
	}

	private Orders saveOrderLocally(Order orderSquare, OrderDTO orderLocal) {

		Orders dbOrder = new Orders.Builder(orderSquare.getId())
							.status("ORDER PLACED")
							.totalPrice(orderSquare.getTotalMoney().getAmount() / SMALLEST_CURRENCY_DENOMINATOR)
							.placedAt(orderLocal.getPlacedAt())
							.completedAt(orderLocal.getCompletedAt())
							.pickupDate(orderLocal.getPickupDate())
							.pickupTime(orderLocal.getPickupTime())
							.customer(new Customer.Builder(orderLocal.getCustomer().getId()).build())
							.build();

		Orders confirmation = orderRepository.save(dbOrder);
		feed.send(confirmation, "new");

		return confirmation;
	}

	private Order saveOrderInSquare(OrderDTO order) {

		Order orderSquare = transferToSquareOrder(order);

		CreateOrderRequest body = new CreateOrderRequest.Builder().idempotencyKey(UUID.randomUUID().toString())
				.order(orderSquare).build();

		try {
			return client.getOrdersApi().createOrder(System.getenv("SQUARE_LOCATION"), body).getOrder();
		} catch (ApiException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private Order transferToSquareOrder(OrderDTO order) {
		List<OrderLineItem> itemsToSaveSquare = new ArrayList<OrderLineItem>();

		order.getOrderItems().forEach(orderItem -> {
			List<OrderLineItemModifier> itemModifiersSquare = transferToListOfSquareModifiers(
					orderItem.getSelectedModifiers());
			OrderLineItem orderItemSquare = transferToSquareOrderItem(orderItem, itemModifiersSquare);

			itemsToSaveSquare.add(orderItemSquare);
		});

		Order orderSquare = new Order.Builder(System.getenv("SQUARE_LOCATION")).lineItems(itemsToSaveSquare).build();

		return orderSquare;
	}

	private OrderLineItem transferToSquareOrderItem(OrderItemDTO item, List<OrderLineItemModifier> modifiers) {
		// Add all the variation data
		OrderLineItem orderLineItem = new OrderLineItem.Builder(item.getQuantity() + "")
				.catalogObjectId(item.getVariationId())
				.modifiers(modifiers)
				.build();
		return orderLineItem;
	}

	private List<OrderLineItemModifier> transferToListOfSquareModifiers(List<OrderModifierDTO> selectedModifiers) {

		List<OrderLineItemModifier> itemModifiersSquare = new ArrayList<OrderLineItemModifier>();

		if (selectedModifiers != null) {
			selectedModifiers.forEach(selectedModifier -> {

				OrderLineItemModifier modifierSquare = new OrderLineItemModifier.Builder()
						.catalogObjectId(selectedModifier.getId()).build();

				itemModifiersSquare.add(modifierSquare);
			});
		}

		return itemModifiersSquare;
	}

	public boolean pay(PaymentDTO paymentDTO) {

		checkParametersForPayment(paymentDTO);
		Payment paymentInSquare = payInSquare(paymentDTO);
		savePaymentLocally(paymentInSquare);
		
		return true;
	}

	private void savePaymentLocally(Payment paymentInSquare) {
		upcafe.entity.orders.Payment paymentConfirmation = new upcafe.entity.orders.Payment
									.Builder(paymentInSquare.getId())
									.totalPaid((double) paymentInSquare.getAmountMoney().getAmount() / SMALLEST_CURRENCY_DENOMINATOR)
									.receiptUrl(paymentInSquare.getReceiptUrl())
									.status(paymentInSquare.getStatus())
									.paymentMadeAt(paymentInSquare.getUpdatedAt())
									.order(new Orders.Builder(paymentInSquare.getOrderId()).build())
									.build();


		paymentRepository.save(paymentConfirmation);
	}

	private Payment payInSquare(PaymentDTO payment) {

		Money amountMoney = new Money.Builder()
								.currency("USD")
								.amount((long) (payment.getPrice() * SMALLEST_CURRENCY_DENOMINATOR))
								.build();

		CreatePaymentRequest body = new CreatePaymentRequest
									.Builder(payment.getNonce(), UUID.randomUUID().toString(),amountMoney)
									.autocomplete(true)
									.locationId(System.getenv("SQUARE_LOCATION"))
									.orderId(payment.getOrderId())
									.build();
		
		try {
			return client.getPaymentsApi().createPayment(body).getPayment();
		}
		catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private boolean checkParametersForPayment(PaymentDTO payment) {
		if (payment.getNonce() == null)
			throw new MissingParameterException("nonce");

		if (payment.getOrderId() == null)
			throw new MissingParameterException("order id");

		if (payment.getPrice() == 0)
			throw new MissingParameterException("price");

		return true;
	}

	// public Collection<OrderData> getOrdersByState(String state) {

	// 	// Get the IDs of the orders with that state from the local repository

	// 	Hashtable<String, OrderData> orders = new Hashtable<String, OrderData>();
	// 	List<String> orderIdsToRetrieve = new ArrayList<String>();

	// 	orderRepository.getOrdersByState(state).forEach(order -> {
	// 		OrderData data = new OrderData();
	// 		data.setCreatedAt(order.getCreatedAt());
	// 		data.setCustomer(order.getCustomer());
	// 		data.setPickupTime(order.getPickupTime());
	// 		data.setState(order.getState());
	// 		data.setTotalPrice(order.getTotalPrice());
	// 		data.setId(order.getId());
	// 		data.setPickupDate(order.getPickupDate());
	// 		orders.put(data.getId(), data);
	// 		orderIdsToRetrieve.add(data.getId());
	// 	});

		
	// 	return getOrdersByIdFromSquare(orderIdsToRetrieve, orders);

	// }

	// private Collection<OrderData> getOrdersByIdFromSquare(List<String> ids, Hashtable<String, OrderData> orders) {
	// 	if (ids.size() > 0) {

	// 		// Get the actual items for each order in Square
	// 		BatchRetrieveOrdersRequest request = new BatchRetrieveOrdersRequest.Builder(ids).build();

	// 		try {

	// 			// Collect order information from Square

	// 			BatchRetrieveOrdersResponse response = client.getOrdersApi()
	// 					.batchRetrieveOrders(System.getenv("SQUARE_LOCATION"), request);

	// 			// For each returned order, collect the item information
	// 			response.getOrders().forEach(order -> {

	// 				List<SelectedItem> items = new ArrayList<SelectedItem>();

	// 				// For each item in the order collect its information
	// 				order.getLineItems().forEach(squareItem -> {

	// 					SelectedItem item = new SelectedItem();
	// 					item.setQuantity(Integer.parseInt(squareItem.getQuantity()));
	// 					item.setPrice((double) squareItem.getGrossSalesMoney().getAmount() / 100);

	// 					VariationData variation = new VariationData();

	// 					if (squareItem.getVariationName().compareTo("Regular") == 0)
	// 						variation.setName(squareItem.getName());
	// 					else
	// 						variation.setName(squareItem.getVariationName());

	// 					variation.setVariationPrice((double) squareItem.getVariationTotalPriceMoney().getAmount() / 100);
	// 					variation.setVariationId(squareItem.getCatalogObjectId());

	// 					item.setVariationData(variation);

	// 					ArrayList<ModifierData> modifiers = new ArrayList<ModifierData>();

	// 					if (squareItem.getModifiers() != null) {
	// 						// For each modifier corresponding to this item, collect the modifier
	// 						// information
	// 						squareItem.getModifiers().forEach(squareModifier -> {
	// 							ModifierData modifier = new ModifierData();
	// 							modifier.setId(squareModifier.getCatalogObjectId());
	// 							modifier.setName(squareModifier.getName());
	// 							modifier.setPrice((double) squareModifier.getTotalPriceMoney().getAmount() / 100);

	// 							modifiers.add(modifier);
	// 						}); // end forEach modifier in item

	// 						item.setSelectedModifiers(modifiers);
	// 					}
	// 					items.add(item);

	// 				}); // end forEach item in Square order

	// 				// Add the line items to the order
	// 				String orderId = order.getId();

	// 				if (orders.containsKey(orderId))
	// 					orders.get(orderId).setSelectedLineItems(items);
	// 				else
	// 					System.out.println("Something went seriously wrong.");

	// 			}); // end forEach Square order

	// 			orders.values().forEach(order -> {
	// 				System.out.println(order);
	// 			});

	// 		} catch (ApiException | IOException e) {
	// 			// TODO Auto-generated catch block
	// 			e.printStackTrace();
	// 		}
	// 	}
		
	// 	return orders.values();

	// }

	// public void changeState(String state, Orders order) {

	// 	System.out.println("\t\t\t\tSAVING - - - - - - - - - - - - - - - -  -\n" + order);
	// 	order.setState(state.toUpperCase());

	// 	orderRepository.save(order);
		
	// 	if(state.compareTo("ORDER PLACED") == 0)
	// 		feed.send(order, "NEW");
			
	// 	feed.send(order, state);
	// }

	// public Orders getActiveCustomerOrder(int customerId) {
	// 	return orderRepository.getActiveOrdersByCustomerId(customerId);
	// }
}
