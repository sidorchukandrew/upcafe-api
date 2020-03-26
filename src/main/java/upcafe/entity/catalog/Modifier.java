package upcafe.entity.catalog;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Modifier {

	@Id
	private String id;											// A_SPECIFIC_MODIFIER_FROM_LIST_ID
	private double price;											// 200
	
	@ManyToOne
	@JoinColumn(name = "list_id", referencedColumnName = "id")
	private ModifierList modList;								// THE_LIST_THIS_MODIFIER_BELONGS_TO,   ex:  VEGETABLES
	
	private boolean onByDefault;								// true or false
	private String name;										// Name of the food of the modifier
//	private boolean inStock;
	private String batchUpdateId;
	private String updatedAt;
	
	public Modifier(String id, double price, ModifierList modList, boolean onByDefault, String name,
			String batchUpdateId, String updatedAt) {
		super();
		this.id = id;
		this.price = price;
		this.modList = modList;
		this.onByDefault = onByDefault;
		this.name = name;
		this.batchUpdateId = batchUpdateId;
		this.updatedAt = updatedAt;
	}

	public Modifier() { }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public ModifierList getModifierList() {
		return modList;
	}

	public void setModifierList(ModifierList modList) {
		this.modList = modList;
	}

	public boolean isOnByDefault() {
		return onByDefault;
	}

	public void setOnByDefault(boolean onByDefault) {
		this.onByDefault = onByDefault;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
//	public boolean isInStock() {
//		return inStock;
//	}
//	
//	public void setInStock(boolean inStock) {
//		this.inStock = inStock;
//	}
	
	public String getBatchUpdateId() {
		return batchUpdateId;
	}

	public void setBatchUpdateId(String batchUpdateId) {
		this.batchUpdateId = batchUpdateId;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "Modifier [id=" + id + ", price=" + price + ", modList=" + modList + ", onByDefault=" + onByDefault
				+ ", name=" + name + ", batchUpdateId=" + batchUpdateId + ", updatedAt=" + updatedAt + "]";
	}

}
