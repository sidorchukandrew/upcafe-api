package com.sidorchuk.upcafe.resource.entity.catalog;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class ModifierList {

	@Id
	private String id;										// A_MODIFIER_LIST_THAT_CAN_BE_APPLIED
	private String name;									// VEGETABLES
	private String selectionType;							// MULTIPLE or SINGLE?
	
	@OneToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "image_id", referencedColumnName = "id")
	private Image image;
	
	public ModifierList(String id, String name, String selectionType, Image image) {
		super();
		this.id = id;
		this.name = name;
		this.selectionType = selectionType;
		this.image = image;
	}
	
	public ModifierList() { }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSelectionType() {
		return selectionType;
	}

	public void setSelectionType(String selectionType) {
		this.selectionType = selectionType;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "ModifierList [id=" + id + ", name=" + name + ", selectionType=" + selectionType + ", image=" + image
				+ "]";
	}
}
