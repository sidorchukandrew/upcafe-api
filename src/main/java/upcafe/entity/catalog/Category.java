package upcafe.entity.catalog;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Category {

	@Id
	@Column(length = 36)
	private String id;

	@Column(length = 36)
	private String name;

	@Column(length = 36)
	private String batchUpdateId;

	@JsonFormat(pattern = "EEE MMM dd yyyy HH:mm:ss")
	private LocalDateTime updatedAt;

	@OneToMany(mappedBy = "category")
	@JsonManagedReference
	private List<Item> items;

	public Category(String id, String name, String batchUpdateId, LocalDateTime updatedAt, List<Item> items) {
		this.id = id;
		this.name = name;
		this.batchUpdateId = batchUpdateId;
		this.updatedAt = updatedAt;
		this.items = items;
	}

	public Category() {
	}

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

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", batchUpdateId=" + batchUpdateId + ", updatedAt=" + updatedAt
				+ ", items=" + items + "]";
	}
}
