package com.sidorchuk.upcafe.resource.entity.catalog;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "variation")
public class ItemVariation {

	@Id
	@Column(name = "id")
	private String variationId; 			// THIS_VARIATION_TO_THE_ITEM_ID_222

	private String name; 					// TURKEY
	private double price; 					// 200
//	private boolean inStock;
	
	@OneToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "image_id", referencedColumnName = "id")
	private Image image;

	@ManyToOne
	@JoinColumn(name = "item_id", referencedColumnName = "id")
	private Item item; 					// THIS_ITEM_ID_111
	
	private String batchUpdateId;
	private String updatedAt;

	public ItemVariation(String variationId, String name, double price, Image image, Item item, String batchUpdateId,
			String updatedAt) {
		super();
		this.variationId = variationId;
		this.name = name;
		this.price = price;
		this.image = image;
		this.item = item;
		this.batchUpdateId = batchUpdateId;
		this.updatedAt = updatedAt;
	}

	public ItemVariation() {
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public String getVariationId() {
		return variationId;
	}

	public void setVariationId(String variationId) {
		this.variationId = variationId;
	}

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

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

//	public boolean isInStock() {
//		return inStock;
//	}

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
		return "ItemVariation [variationId=" + variationId + ", name=" + name + ", price=" + price + ", image=" + image
				+ ", item=" + item + ", batchUpdateId=" + batchUpdateId + ", updatedAt=" + updatedAt + "]";
	}
}