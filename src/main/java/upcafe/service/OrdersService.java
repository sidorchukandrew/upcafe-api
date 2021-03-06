package upcafe.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.squareup.square.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.squareup.square.SquareClient;
import com.squareup.square.exceptions.ApiException;

import upcafe.controller.FeedController;
import upcafe.dto.order.OrderDTO;
import upcafe.dto.order.OrderItemDTO;
import upcafe.dto.order.OrderModifierDTO;
import upcafe.dto.order.PaymentDTO;
import upcafe.dto.users.UserDTO;
import upcafe.entity.orders.Orders;
import upcafe.entity.signin.User;
import upcafe.error.MissingParameterException;
import upcafe.repository.orders.OrderRepository;
import upcafe.repository.orders.PaymentRepository;
import upcafe.utils.Logger;

@Service
public class OrdersService {

    private static final int SMALLEST_CURRENCY_DENOMINATOR = 100;
    private static final int EAST_US_TIMEZONE_SHIFT = 4;
    @Autowired
    private SquareClient client;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private FeedController feed;

    public Collection<OrderDTO> getOrdersByDate(String dateString) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy");
        LocalDate date = LocalDate.parse(dateString, formatter);

        Hashtable<String, OrderDTO> orders = new Hashtable<String, OrderDTO>();
        List<String> orderIdsToRetrieve = new ArrayList<String>();

        orderRepository.getOrdersByPickupDate(date).forEach(order -> {

            UserDTO customer = new UserDTO.Builder(order.getCustomer().getEmail())
                    .name(order.getCustomer().getName())
                    .build();

            OrderDTO data = new OrderDTO.Builder()
                    .placedAt(order.getPlacedAt())
                    .status(order.getStatus())
                    .pickupTime(order.getPickupTime())
                    .pickupDate(order.getPickupDate())
                    .totalPrice(order.getTotalPrice())
                    .id(order.getId())
                    .customer(customer)
                    .completedAt(order.getCompletedAt())
                    .build();

            orders.put(data.getId(), data);
            orderIdsToRetrieve.add(data.getId());
        });

        return getOrdersByIdFromSquare(orderIdsToRetrieve, orders);
    }

    public Orders createOrder(OrderDTO orderDTO) {

        checkParametersForOrder(orderDTO);

        Order orderSquare = saveOrderInSquare(orderDTO);
        return saveOrderLocally(orderSquare, orderDTO);
    }

    private void checkParametersForOrder(OrderDTO order) {

        StringBuilder missingParameters = new StringBuilder("");

        if (order.getCustomer() == null)
            missingParameters.append("customer id, ");

        if (order.getTotalPrice() <= 0)
            missingParameters.append("total price, ");

        if (order.getOrderItems() == null) {
            missingParameters.append("order items");
        } else {
            order.getOrderItems().forEach(item -> {

                if (item.getVariationId() == null || item.getVariationId().compareTo("") == 0)
                    missingParameters.append("variation id");
            });
        }

        if (missingParameters.length() > 0)
            throw new MissingParameterException(missingParameters.toString());
    }

    private Orders saveOrderLocally(Order orderSquare, OrderDTO orderLocal) {

        Orders dbOrder = new Orders.Builder(orderSquare.getId())
                .status("ORDER PLACED")
                .totalPrice((double) orderSquare.getTotalMoney().getAmount() / SMALLEST_CURRENCY_DENOMINATOR)
                .placedAt(LocalDateTime.now().minusHours(EAST_US_TIMEZONE_SHIFT))
                .completedAt(orderLocal.getCompletedAt())
                .pickupDate(orderLocal.getPickupDate())
                .pickupTime(orderLocal.getPickupTime())
                .customer(new User.Builder(orderLocal.getCustomer().getEmail())
                        .id(orderLocal.getCustomer().getId())
                        .build())
                .build();

        Orders confirmation = orderRepository.save(dbOrder);
        feed.send(orderLocal, "new");

        return confirmation;
    }

    private Order saveOrderInSquare(OrderDTO order) {

        Order orderSquare = transferToSquareOrder(order);

        CreateOrderRequest body = new CreateOrderRequest.Builder().idempotencyKey(UUID.randomUUID().toString())
                .order(orderSquare).build();

        try {
            return client.getOrdersApi().createOrder(System.getenv("SQUARE_LOCATION"), body).getOrder();
        } catch (ApiException e) {
        	Logger.logApiExceptions(e);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Retrying now");
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

        OrderLineItemTax orderLineItemTax = new OrderLineItemTax.Builder()
                .scope("ORDER")
                .catalogObjectId("O3CFMUMRYILCUZZAS2KGOLGA")
                .build();

        Order orderSquare = new Order.Builder(System.getenv("SQUARE_LOCATION"))
                .lineItems(itemsToSaveSquare)
                .taxes(Arrays.asList(orderLineItemTax))
                .build();

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
                .Builder(payment.getNonce(), UUID.randomUUID().toString(), amountMoney)
                .autocomplete(true)
                .locationId(System.getenv("SQUARE_LOCATION"))
                .orderId(payment.getOrderId())
                .build();

        try {
            return client.getPaymentsApi().createPayment(body).getPayment();
        } catch (ApiException e) {
           
        	Logger.logApiExceptions(e);
            return null;
        } catch (IOException e) {
            System.out.println("Retrying payment");
            e.printStackTrace();
            return payInSquare(payment);
        }

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

    private Collection<OrderDTO> getOrdersByIdFromSquare(List<String> ids, Hashtable<String, OrderDTO> orders) {
        if (ids.size() > 0) {

            // Get the actual items for each order in Square
            BatchRetrieveOrdersRequest request = new BatchRetrieveOrdersRequest.Builder(ids).build();

            try {

                // Collect order information from Square

                BatchRetrieveOrdersResponse response = client.getOrdersApi()
                        .batchRetrieveOrders(System.getenv("SQUARE_LOCATION"), request);

                // For each returned order, collect the item information
                response.getOrders().forEach(order -> {

                    List<OrderItemDTO> items = new ArrayList<OrderItemDTO>();

                    // For each item in the order collect its information
                    order.getLineItems().forEach(squareItem -> {

                        ArrayList<OrderModifierDTO> modifiers = new ArrayList<OrderModifierDTO>();

                        if (squareItem.getModifiers() != null) {
                            // For each modifier corresponding to this item, collect the modifier
                            // information
                            squareItem.getModifiers().forEach(squareModifier -> {
                                OrderModifierDTO modifier = new OrderModifierDTO
                                        .Builder(squareModifier.getCatalogObjectId())
                                        .name(squareModifier.getName())
                                        .price((double) squareModifier.getTotalPriceMoney().getAmount()
                                                / SMALLEST_CURRENCY_DENOMINATOR)
                                        .build();

                                modifiers.add(modifier);
                            }); // end forEach modifier in item
                        }

                        String nameOfItem = squareItem.getVariationName().compareTo("Regular") == 0
                                ? squareItem.getName()
                                : squareItem.getVariationName();

                        OrderItemDTO item = new OrderItemDTO.Builder(squareItem.getCatalogObjectId())
                                .quantity(Integer.parseInt(squareItem.getQuantity()))
                                .price((double) squareItem.getVariationTotalPriceMoney().getAmount() / SMALLEST_CURRENCY_DENOMINATOR)
                                .name(nameOfItem)
                                .selectedModifiers(modifiers)
                                .build();

                        items.add(item);

                    }); // end forEach item in Square order

                    // Add the line items to the order
                    String orderId = order.getId();

                    if (orders.containsKey(orderId))
                        orders.get(orderId).setOrderItems(items);
                    else
                        System.out.println("Something went seriously wrong.");

                }); // end forEach Square order


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ApiException e) {
            	Logger.logApiExceptions(e);
            }
        }

        return orders.values();

    }

    public void changeStatus(String status, OrderDTO order) {

        if (order.getId() == null)
            throw new MissingParameterException("order id");

        System.out.println("\t\t\t\tSAVING - - - - - - - - - - - - - - - -  -\n" + order);
        order.setStatus(status.toUpperCase());

        if (status.compareTo("COMPLETE") == 0) {
            orderRepository.updateOrderToCompleted(order.getId(), status, LocalDateTime.now().minusHours(EAST_US_TIMEZONE_SHIFT));
        } else {
            orderRepository.updateStatusForOrderWithId(order.getId(), status);
        }

        if (status.compareTo("ORDER PLACED") == 0)
            feed.send(order, "NEW");

        else {
            feed.send(order, status);
        }
    }

    // TODO: change to get by email
    public OrderDTO getActiveCustomerOrder(int customerId) {
        Orders orderDB = orderRepository.getActiveOrdersByCustomerId(customerId);
        if (orderDB != null) {

            OrderDTO orderDTO = new OrderDTO.Builder()
                    .id(orderDB.getId())
                    .status(orderDB.getStatus())
                    .pickupDate(orderDB.getPickupDate())
                    .pickupTime(orderDB.getPickupTime())
                    .placedAt(orderDB.getPlacedAt())
                    .totalPrice(orderDB.getTotalPrice())
                    .orderItems(getOrderItemsFromSquare(orderDB.getId()))
                    .build();

            return orderDTO;
        }

        return null;
    }

    private List<OrderItemDTO> getOrderItemsFromSquare(String id) {

        List<OrderItemDTO> orderItems = new ArrayList<OrderItemDTO>();

        List<String> ids = new ArrayList<String>();

        ids.add(id);
        BatchRetrieveOrdersRequest request = new BatchRetrieveOrdersRequest.Builder(ids).build();

        try {
            BatchRetrieveOrdersResponse response = client.getOrdersApi()
                    .batchRetrieveOrders(System.getenv("SQUARE_LOCATION"), request);

            response.getOrders().forEach(orderSquare -> {

                orderSquare.getLineItems().forEach(squareItem -> {

                    ArrayList<OrderModifierDTO> modifiers = new ArrayList<OrderModifierDTO>();

                    if (squareItem.getModifiers() != null) {

                        squareItem.getModifiers().forEach(squareModifier -> {
                            OrderModifierDTO modifier = new OrderModifierDTO
                                    .Builder(squareModifier.getCatalogObjectId())
                                    .name(squareModifier.getName())
                                    .price((double) squareModifier.getTotalPriceMoney().getAmount()
                                            / SMALLEST_CURRENCY_DENOMINATOR)
                                    .build();

                            modifiers.add(modifier);
                        }); // end forEach modifier in item
                    }

                    String nameOfItem = squareItem.getVariationName().compareTo("Regular") == 0
                            ? squareItem.getName()
                            : squareItem.getVariationName();

                    OrderItemDTO item = new OrderItemDTO.Builder(squareItem.getCatalogObjectId())
                            .quantity(Integer.parseInt(squareItem.getQuantity()))
                            .price((double) squareItem.getVariationTotalPriceMoney().getAmount() / SMALLEST_CURRENCY_DENOMINATOR)
                            .name(nameOfItem)
                            .selectedModifiers(modifiers)
                            .build();

                    orderItems.add(item);

                }); // end forEach item in Square order

                return;
            }); // end forEach Square order

            return orderItems;
        } catch (ApiException e) {
           Logger.logApiExceptions(e);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
