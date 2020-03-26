package upcafe.model.catalog;

public class ModifierData {

	private String name;
	private double price;
	private boolean onByDefault;
	private boolean inStock;
	private String id;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public boolean isOnByDefault() {
		return onByDefault;
	}
	
	public void setOnByDefault(boolean onByDefault) {
		this.onByDefault = onByDefault;
	}
	
	public boolean isInStock() {
		return inStock;
	}
	
	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "\n\t\t\tModifierData [name=" + name + ", price=" + price + ", onByDefault=" + onByDefault + ", inStock="
				+ inStock + ", id=" + id + "]";
	}
}
