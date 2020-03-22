package com.sidorchuk.upcafe.resource.model.catalog;

import com.sidorchuk.upcafe.resource.entity.catalog.Image;

public class ItemData {

	private String name;
	private String description;
	private boolean inStock;
	private Image image;
	
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
	
	public boolean isInStock() {
		return inStock;
	}
	
	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "ItemData [name=" + name + ", description=" + description + ", inStock=" + inStock + ", image=" + image
				+ "]";
	}
	
}
