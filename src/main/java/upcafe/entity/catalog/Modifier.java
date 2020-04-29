package upcafe.entity.catalog;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Modifier {

	@Id
	@Column(length = 36)
	private String id;

	@Column(precision = 4)
	private double price;

	@Column(length = 36)
	private String name;

	@ManyToOne
	@JoinColumn(name = "list_id", referencedColumnName = "id")
	private ModifierList modList;

	private boolean onByDefault;

	private boolean inStock;

	@Column(length = 36)
	private String batchUpdateId;

	@JsonFormat(pattern = "EEE MMM dd yyyy HH:mm:ss")
	private LocalDateTime updatedAt;

	@OneToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "image_id", referencedColumnName = "id")
	private Image image;

	public Modifier(String id, double price, ModifierList modList, boolean onByDefault, String name,
			String batchUpdateId, LocalDateTime updatedAt, boolean inStock, Image image) {
		super();
		this.id = id;
		this.price = price;
		this.modList = modList;
		this.onByDefault = onByDefault;
		this.name = name;
		this.batchUpdateId = batchUpdateId;
		this.updatedAt = updatedAt;
		this.inStock = inStock;
		this.image = image;
	}

	public Modifier() {
	}

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

	public boolean isInStock() {
		return inStock;
	}

	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}

	public String getBatchUpdateId() {
		return batchUpdateId;
	}

	public void setBatchUpdateId(String batchUpdateId) {
		this.batchUpdateId = batchUpdateId;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Image getImage() {
		return image;
	}

	public void setModList(ModifierList modList) {
		this.modList = modList;
	}

	public ModifierList getModList() {
		return modList;
	}

	@Override
	public String toString() {
		return "{" + " id='" + getId() + "'" + ", price='" + getPrice() + "'" + ", name='" + getName() + "'"
				+ ", modList='" + getModList() + "'" + ", onByDefault='" + isOnByDefault() + "'" + ", inStock='"
				+ isInStock() + "'" + ", batchUpdateId='" + getBatchUpdateId() + "'" + ", updatedAt='" + getUpdatedAt()
				+ "'" + ", image='" + getImage() + "'" + "}";
	}

}
