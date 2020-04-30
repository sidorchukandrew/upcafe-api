package upcafe.entity.catalog;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

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

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
	@JsonBackReference
	private Category category;

	@OneToMany(mappedBy = "item")
	private List<Variation> variations;

	@OneToMany(mappedBy = "item")
	private List<ItemModifierList> modifierLists;

	public Item(String name, String description, String id, Category category, String batchUpdateId,
			LocalDateTime updatedAt, List<ItemModifierList> modifierLists) {
		super();
		this.name = name;
		this.description = description;
		this.id = id;
		this.category = category;
		this.batchUpdateId = batchUpdateId;
		this.updatedAt = updatedAt;
		this.modifierLists = modifierLists;
	}

	public Item() {
	}

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

	public void setId(String id) {
		this.id = id;
	}

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

	public void setModifierLists(List<ItemModifierList> modifierLists) {
		this.modifierLists = modifierLists;
	}

	public List<ItemModifierList> getModifierLists() {
		return modifierLists;
	}

	public List<Variation> getVariations() {
		return variations;
	}

	public void setVariations(List<Variation> variations) {
		this.variations = variations;
	}

	@Override
	public String toString() {
		return "{" + " id='" + getId() + "'" + ", name='" + getName() + "'" + ", description='" + getDescription() + "'"
				+ ", batchUpdateId='" + getBatchUpdateId() + "'" + ", updatedAt='" + getUpdatedAt() + "'"
				+ ", category='" + getCategory() + "'" + ", variations='" + getVariations() + "'" + ", modifierLists='"
				+ getModifierLists() + "'" + "}";
	}

}
