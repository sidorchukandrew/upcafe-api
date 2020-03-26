package upcafe.model.orders;

import java.util.ArrayList;
import java.util.List;

public class OrderData {
	private List<SelectedItem> selectedLineItems;
	private double totalPrice;

	public OrderData(ArrayList<SelectedItem> selectedLineItems, double totalPrice) {
		super();
		this.selectedLineItems = selectedLineItems;
		this.totalPrice = totalPrice;
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

	@Override
	public String toString() {
		return "OrderData [selectedLineItems=" + selectedLineItems + ", totalPrice=" + totalPrice + "]";
	}
}
