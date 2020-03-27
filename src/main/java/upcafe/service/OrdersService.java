package upcafe.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

import upcafe.entity.orders.Orders;
import upcafe.entity.signin.Customer;
import upcafe.model.orders.OrderConfirmation;
import upcafe.model.orders.OrderData;
import upcafe.model.orders.PaymentData;
import upcafe.repository.orders.OrderRepository;
import upcafe.repository.orders.PaymentRepository;

@Service
public class OrdersService {

	@Autowired private SquareClient client;
	@Autowired private OrderRepository orderRepository;
	@Autowired private PaymentRepository paymentRepository;
	
	public OrderConfirmation createOrder(OrderData orderData) {
		
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
			dbOrder.setState(response.getOrder().getState());
			dbOrder.setTotalPrice((double)response.getOrder().getTotalMoney().getAmount() / 100);
			
			Customer tempCustomer = new Customer();
			tempCustomer.setId(5);
			
			dbOrder.setCustomer(tempCustomer);
			
			System.out.println(dbOrder.toString());
			orderRepository.save(dbOrder);
			
			OrderConfirmation confirmation = new OrderConfirmation();
			confirmation.setCreatedAt(response.getOrder().getCreatedAt());
			confirmation.setId(response.getOrder().getId());
			confirmation.setState(response.getOrder().getState());
			confirmation.setTotalPrice((double)response.getOrder().getTotalMoney().getAmount() / 100);
			System.out.println(confirmation);
			
			return confirmation;
		} catch (ApiException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void pay(PaymentData payment) {
		
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
			tempCustomer.setId(5);
			
			paymentConfirmation.setCustomer(tempCustomer);
			
			Orders tempOrder = new Orders();
			tempOrder.setId(response.getPayment().getOrderId());
			paymentConfirmation.setOrder(tempOrder);
			
			paymentRepository.save(paymentConfirmation);
			
			System.out.println(paymentConfirmation.toString());
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
