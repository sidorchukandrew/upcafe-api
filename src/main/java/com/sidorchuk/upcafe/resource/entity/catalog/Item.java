package com.sidorchuk.upcafe.resource.entity.catalog;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Item {

	@Id
	@Column(name = "id")
	private String itemId; 						// THIS_ITEM_ID_111
	private String name; 						// Sandwich
	private String description; 				// A delicious toasted sandwich.
//	private boolean inStock;

	@OneToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "image_id", referencedColumnName = "id")
	private Image image;
	
	@ManyToOne
	@JoinColumn(name = "category_id", referencedColumnName = "id")
	private Category category;
	
	public Item(String name, String description, String itemId, Image image, Category category) {
		super();
		this.name = name;
		this.description = description;
		this.itemId = itemId;
		this.image = image;
		this.category = category;
//		this.inStock = isInStock;
	}
	
	public Item() { }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
//	public boolean isInStock() {
//		return inStock;
//	}
//	
//	public void setInStock(boolean inStock) {
//		this.inStock = inStock;
//	}

	@Override
	public String toString() {
		return "Item [name=" + name + ", description=" + description + ", itemId=" + itemId + ", image=" + image
				+ ", categoryId=" + category+ "]";
	}

}
