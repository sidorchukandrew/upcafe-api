package upcafe.v2.service;

import com.squareup.square.SquareClient;
import com.squareup.square.exceptions.ApiException;
import com.squareup.square.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upcafe.utils.Logger;
import upcafe.utils.SquareUtils;
import upcafe.utils.TransferUtils;
import upcafe.v2.enums.PickupState;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
public class OrdersServiceV2 {

    @Autowired
    private SquareClient SQUARE;

    public int getMostRecentVersionByOrderId(String orderId) {
       return getOrderById(orderId).getVersion();
    }

    public upcafe.v2.models.Order updateOrderFulfillmentState(String orderId, String fulfillmentId, String requestedState, int version) {

        String newState = "";
        if(areEqual(requestedState, PickupState.PROPOSED.toString())) {
            newState = PickupState.PROPOSED.toString();
        }
        else if(areEqual(requestedState, PickupState.RESERVED.toString())) {
            newState = PickupState.RESERVED.toString();
        }
        else if(areEqual(requestedState, PickupState.PREPARED.toString())) {
            newState = PickupState.PREPARED.toString();
        }
        else if(areEqual(requestedState, PickupState.COMPLETED.toString())) {
            newState = PickupState.COMPLETED.toString();
        }
        else {
            // THROW HERE : Unknown fulfillment state
        }

        OrderFulfillment fulfillment = new OrderFulfillment.Builder()
                .uid(fulfillmentId)
                .state(newState)
                .build();

        Order order = new Order.Builder(SquareUtils.getSquareLocationId())
                .fulfillments(Arrays.asList(fulfillment))
                .version(version)
                .build();


        UpdateOrderRequest body = new UpdateOrderRequest.Builder()
                .order(order)
                .idempotencyKey(SquareUtils.generateRandomId())
                .build();

        try {
            UpdateOrderResponse updateResponse = SQUARE.getOrdersApi().updateOrder(SquareUtils.getSquareLocationId(), orderId, body);
            return TransferUtils.toOrderDTO(updateResponse.getOrder());
        } catch (ApiException e) {
            Logger.logApiExceptions(e);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void createOrder(upcafe.v2.models.Order order) {

        OrderFulfillmentRecipient recipient = new OrderFulfillmentRecipient.Builder()
                .customerId(order.getCustomer().getSquareCustomerId())
                .build();

        OrderFulfillmentPickupDetails pickupDetails = new OrderFulfillmentPickupDetails.Builder()
                .pickupAt(SquareUtils.toUtcTimestamp(order.getPickupDetails().getPickupTimeRequested()))
                .recipient(recipient)
                .build();

        OrderFulfillment fulfillment = new OrderFulfillment.Builder()
                .state(PickupState.PROPOSED.toString())
                .type("PICKUP")
                .pickupDetails(pickupDetails)
                .build();

        Money money = new Money.Builder()
                .amount(100L)
                .currency("USD")
                .build();

        OrderLineItem lineItem = new OrderLineItem.Builder("1")
                .basePriceMoney(money)
                .name("Orange")
                .build();

        Order squareOrder = new Order.Builder(SquareUtils.getSquareLocationId())
                .fulfillments(Arrays.asList(fulfillment))
                .lineItems(Arrays.asList(lineItem))
                .customerId(order.getCustomer().getSquareCustomerId())
                .build();

        CreateOrderRequest body = new CreateOrderRequest.Builder()
                .order(squareOrder)
                .idempotencyKey(SquareUtils.generateRandomId())
                .build();

        try {
            CreateOrderResponse response = SQUARE.getOrdersApi().createOrder(SquareUtils.getSquareLocationId(), body);
            System.out.println(response);
        } catch (ApiException e) {
            Logger.logApiExceptions(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Order> getOrdersForLocation() {
        SearchOrdersRequest request = new SearchOrdersRequest.Builder()
                .locationIds(Arrays.asList(SquareUtils.getSquareLocationId()))
                .build();

        try {
            SearchOrdersResponse response = SQUARE.getOrdersApi().searchOrders(request);
            return response.getOrders();
        } catch (ApiException e) {
            Logger.logApiExceptions(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public upcafe.v2.models.Order getOrderById(String orderId) {
        BatchRetrieveOrdersRequest body = new BatchRetrieveOrdersRequest.Builder(Arrays.asList(orderId))
                .build();

        try {
            BatchRetrieveOrdersResponse response = SQUARE.getOrdersApi().batchRetrieveOrders(SquareUtils.getSquareLocationId(), body);

            if(response.getOrders().size() == 1) {
                return TransferUtils.toOrderDTO(response.getOrders().get(0));
            }

        } catch (ApiException e) {
            Logger.logApiExceptions(e);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Order> getOrdersForDate(String dateText) {
        LocalDate requestedDate = LocalDate.parse(dateText, DateTimeFormatter.ofPattern("M/d/yyyy"));

        String startingAt = SquareUtils.toUtcTimestamp(requestedDate.atStartOfDay());
        String endingAt = SquareUtils.toUtcTimestamp(requestedDate.atTime(23, 59, 59));

        TimeRange createdAtRange = new TimeRange.Builder()
                .startAt(startingAt)
                .endAt(endingAt)
                .build();

        SearchOrdersDateTimeFilter dateTimeFilter = new SearchOrdersDateTimeFilter.Builder()
                .createdAt(createdAtRange)
                .build();

        SearchOrdersFilter filter = new SearchOrdersFilter.Builder()
                .dateTimeFilter(dateTimeFilter)
                .build();

        SearchOrdersQuery searchQuery = new SearchOrdersQuery.Builder()
                .filter(filter)
                .build();

        SearchOrdersRequest body = new SearchOrdersRequest.Builder()
                .locationIds(Arrays.asList(SquareUtils.getSquareLocationId()))
                .query(searchQuery)
                .build();

        try {
            SearchOrdersResponse response = SQUARE.getOrdersApi().searchOrders(body);
            return response.getOrders();
        } catch (ApiException e) {
            Logger.logApiExceptions(e);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean areEqual(String textOne, String textTwo) {
        return textOne.compareTo(textTwo) == 0;
    }
}
