package upcafe.entity.catalog;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Category {

	@Id
	@Column(length = 36)
	private String id;

	@Column(length = 36)
	private String name;

	@Column(length = 36)
	private String batchUpdateId;

	private LocalDateTime lastUpdated;

	@OneToMany(mappedBy = "category")
	private List<Item> items;

	public static class Builder {
		private final String id;
		private String name;
		private String batchUpdateId;
		private LocalDateTime lastUpdated;
		private List<Item> items;

		public Builder(String id) {
			this.id = id;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder batchUpdateId(String batchUpdateId) {
			this.batchUpdateId = batchUpdateId;
			return this;
		}

		public Builder lastUpdated(LocalDateTime lastUpdated) {
			this.lastUpdated = lastUpdated;
			return this;
		}

		public Builder items(List<Item> items) {
			this.items = items;
			return this;
		}

		public Category build() {
			return new Category(this);
		}
	}

	public Category() {
	}

	private Category(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.batchUpdateId = builder.batchUpdateId;
		this.items = builder.items;
		this.lastUpdated = builder.lastUpdated;
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

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime updatedAt) {
		this.lastUpdated = updatedAt;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", batchUpdateId=" + batchUpdateId + ", updatedAt="
				+ lastUpdated + ", items=" + items + "]";
	}
}
