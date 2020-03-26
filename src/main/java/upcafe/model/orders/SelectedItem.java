package upcafe.model.orders;

import java.util.ArrayList;
import java.util.List;

import upcafe.model.catalog.ModifierData;
import upcafe.model.catalog.VariationData;

public class SelectedItem {

	private int quantity;
	private VariationData variationData;
	private double price;
	private List<ModifierData> selectedModifiers;

	public SelectedItem(int quantity, VariationData variationData, double price,
			ArrayList<ModifierData> selectedModifiers) {
		super();
		this.quantity = quantity;
		this.variationData = variationData;
		this.price = price;
		this.selectedModifiers = selectedModifiers;
	}

	public SelectedItem() {
		super();
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public VariationData getVariationData() {
		return variationData;
	}

	public void setVariationData(VariationData variationData) {
		this.variationData = variationData;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public List<ModifierData> getSelectedModifiers() {
		return selectedModifiers;
	}

	public void setSelectedModifiers(ArrayList<ModifierData> selectedModifiers) {
		this.selectedModifiers = selectedModifiers;
	}

	@Override
	public String toString() {
		return "SelectedLineItem [quantity=" + quantity + ", variationData=" + variationData + ", price=" + price
				+ ", selectedModifiers=" + selectedModifiers + "]";
	}
}
