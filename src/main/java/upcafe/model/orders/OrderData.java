package upcafe.model.orders;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class OrderData {
	private List<SelectedItem> selectedLineItems;
	private double totalPrice;
	private String pickupTime;
	private String state;
	private String createdAt; 
	private String closedAt;
	
	public OrderData(List<SelectedItem> selectedLineItems, double totalPrice, String pickupTime, String state,
			String createdAt, String closedAt) {
		super();
		this.selectedLineItems = selectedLineItems;
		this.totalPrice = totalPrice;
		this.pickupTime = pickupTime;
		this.state = state;
		this.createdAt = createdAt;
		this.closedAt = closedAt;
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

	@Override
	public String toString() {
		return "OrderData [selectedLineItems=" + selectedLineItems + ", totalPrice=" + totalPrice + ", pickupTime="
				+ pickupTime + ", state=" + state + ", createdAt=" + createdAt + ", closedAt=" + closedAt + "]";
	}
}
