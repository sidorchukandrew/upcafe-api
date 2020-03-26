package upcafe.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.squareup.square.SquareClient;
import com.squareup.square.exceptions.ApiException;
import com.squareup.square.models.CreateOrderRequest;
import com.squareup.square.models.CreateOrderResponse;
import com.squareup.square.models.CreatePaymentRequest;
import com.squareup.square.models.CreatePaymentResponse;
import com.squareup.square.models.Money;
import com.squareup.square.models.Order;
import com.squareup.square.models.OrderLineItem;
import com.squareup.square.models.OrderLineItemModifier;

import upcafe.model.orders.OrderData;

@Service
public class OrdersService {

	@Autowired private SquareClient client;
	
	public void createOrder(OrderData orderData) {
		
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
			response.getOrder().getId();
			System.out.println(response);
		} catch (ApiException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void pay() {
		
		Money amountMoney = new Money.Builder()
				.currency("USD")
				.amount(200L)
				.build();
		
		CreatePaymentRequest body = new CreatePaymentRequest.Builder("cnon:card-nonce-ok", UUID.randomUUID().toString(), amountMoney)
				.autocomplete(true)
				.locationId(System.getenv("SQUARE_LOCATION"))
				.orderId("c5vqXMjA4BtNRhX5lA0hAQdd3d4F")
				.build();
		
		try {
			CreatePaymentResponse response = client.getPaymentsApi().createPayment(body);
			System.out.println();
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
