package com.sidorchuk.upcafe.resource.entity.catalog;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Category {

	@Id
	private String id;
	private String name;
	private String batchUpdateId;
	private String updatedAt;
	
	
	public Category(String id, String name, String batchUpdateId, String updatedAt) {
		this.id = id;
		this.name = name;
		this.batchUpdateId = batchUpdateId;
		this.updatedAt = updatedAt;
	}

	public Category() { }

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

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", batchUpdateId=" + batchUpdateId + ", updatedAt=" + updatedAt
				+ "]";
	}
}
