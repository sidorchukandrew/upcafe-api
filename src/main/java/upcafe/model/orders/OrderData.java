package upcafe.model.orders;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import upcafe.entity.signin.Customer;

public class OrderData {
	
	private List<SelectedItem> selectedLineItems;
	private double totalPrice;
	private String pickupTime;
	private String pickupDate;
	private String state;
	private String createdAt; 
	private String closedAt;
	private Customer customer;
	private String id;
	
	public OrderData(String id, List<SelectedItem> selectedLineItems, double totalPrice, String pickupTime, String pickupDate, String state,
			String createdAt, String closedAt, Customer customer) {
		super();
		this.id = id;
		this.selectedLineItems = selectedLineItems;
		this.totalPrice = totalPrice;
		this.pickupTime = pickupTime;
		this.state = state;
		this.createdAt = createdAt;
		this.closedAt = closedAt;
		this.customer = customer;
		this.pickupDate = pickupDate;
	}

	public OrderData() {
		super();
	}

	public List<SelectedItem> getSelectedLineItems() {
		return selectedLineItems;
	}

	public void setSelectedLineItems(List<SelectedItem> selectedItems) {
		this.selectedLineItems = selectedItems;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getClosedAt() {
		return closedAt;
	}

	public void setClosedAt(String closedAt) {
		this.closedAt = closedAt;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPickupDate() {
		return pickupDate;
	}
	
	public void setPickupDate(String date) {
		this.pickupDate = date;
	}

	@Override
	public String toString() {
		return "OrderData [selectedLineItems=" + selectedLineItems + ", totalPrice=" + totalPrice + ", pickupTime="
				+ pickupTime + ", pickupDate=" + pickupDate + ", state=" + state + ", createdAt=" + createdAt
				+ ", closedAt=" + closedAt + ", customer=" + customer + ", id=" + id + "]";
	}

}
