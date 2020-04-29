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
public class Variation {

	@Id
	@Column(length = 36)
	private String id;

	@Column(length = 36)
	private String name;
	private double price;

	private boolean inStock;

	@OneToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "image_id", referencedColumnName = "id")
	private Image image;

	@ManyToOne
	@JoinColumn(name = "item_id", referencedColumnName = "id")
	private Item item;

	@Column(length = 36)
	private String batchUpdateId;

	@JsonFormat(pattern = "EEE MMM dd yyyy HH:mm:ss")
	private LocalDateTime updatedAt;

	public Variation(String id, String name, double price, Image image, Item item, String batchUpdateId,
			LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.image = image;
		this.item = item;
		this.batchUpdateId = batchUpdateId;
		this.updatedAt = updatedAt;
	}

	public Variation() {
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public String getId() {
		return id;
	}

	public void setId(String variationId) {
		this.id = variationId;
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

	public boolean getInStock() {
		return this.inStock;
	}

	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}

	@Override
	public String toString() {
		return "{" + " id='" + getId() + "'" + ", name='" + getName() + "'" + ", price='" + getPrice() + "'"
				+ ", inStock='" + getInStock() + "'" + ", image='" + getImage() + "'" + ", item='" + getItem() + "'"
				+ ", batchUpdateId='" + getBatchUpdateId() + "'" + ", updatedAt='" + getUpdatedAt() + "'" + "}";
	}

}