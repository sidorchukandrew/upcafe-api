package upcafe.entity.catalog;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Item {

	@Id
	@Column(length = 36)
	private String id; 
	
	@Column(length = 52)
	private String name; 						
	private String description; 		

	@Column(length = 36)
	private String batchUpdateId;
	
	@JsonFormat(pattern = "EEE MMM dd yyyy HH:mm:ss")
	private LocalDateTime updatedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	private Category category;

//	@OneToOne(cascade = {CascadeType.ALL})
//	@JoinColumn(name = "image_id", referencedColumnName = "id")
//	private Image image;
	
	
	public Item(String name, String description, String itemId,
//			Image image, 
			Category category, String batchUpdateId, LocalDateTime updatedAt) {
		super();
		this.name = name;
		this.description = description;
		this.id = itemId;
//		this.image = image;
		this.category = category;
		this.batchUpdateId = batchUpdateId;
		this.updatedAt = updatedAt;
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

	public String getId() {
		return id;
	}

	public void setItemId(String id) {
		this.id = id;
	}

//	public Image getImage() {
//		return image;
//	}
//
//	public void setImage(Image image) {
//		this.image = image;
//	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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

	@Override
	public String toString() {
		return "Item [itemId=" + id + ", name=" + name + ", description=" + description + ", batchUpdateId="
				+ batchUpdateId + ", updatedAt=" + updatedAt + ","
//						+ " image=" + image
						+ ", category=" + category + "]";
	}
}
