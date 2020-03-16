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

	public ItemVariation(Item item, String variationId, String name, double price, Image image) {
		this.item = item;
		this.variationId = variationId;
		this.name = name;
		this.price = price;
		this.image = image;
//		this.inStock = inStock;
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

	@Override
	public String toString() {
		return "ItemVariation [item=" + item + ", variationId=" + variationId + ", name=" + name + ", price="
				+ price + ", image=" + image + "]";
	}
}