package upcafe.entity.catalog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class ModifierList {

	@Id
	@Column(length = 36)
	private String id;

	@Column(length = 36)
	private String name;

	@Column(length = 8)
	private String selectionType;

	@OneToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "image_id", referencedColumnName = "id")
	private Image image;

	@Column(length = 36)
	private String batchUpdateId;

	private LocalDateTime lastUpdated;

	@OneToMany(mappedBy = "modList")
	private List<Modifier> modifiers;

	@ManyToMany(mappedBy = "modifierLists")
	Set<Item> items;

	public static class Builder {
		private final String id;
		private String name;
		private Image image;
		private String batchUpdateId;
		private LocalDateTime lastUpdated;
		private List<Modifier> modifiers;
		private Set<Item> items;
		private String selectionType = "";

		public Builder(String id) {
			this.id = id;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder image(Image image) {
			this.image = image;
			return this;
		}

		public Builder batchUpdateId(String batchUpdateId) {
			this.batchUpdateId = batchUpdateId;
			return this;
		}

		public Builder modifiers(List<Modifier> modifiers) {
			this.modifiers = modifiers;
			return this;
		}

		public Builder items(Set<Item> items) {
			this.items = items;
			return this;
		}

		public Builder selectionType(String selectionType) {
			this.selectionType = selectionType;
			return this;
		}

		public Builder lastUpdated(LocalDateTime lastUpdated) {
			this.lastUpdated = lastUpdated;
			return this;
		}

		public ModifierList build() {
			return new ModifierList(this);
		}
	}

	private ModifierList(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.selectionType = builder.selectionType;
		this.image = builder.image;
		this.batchUpdateId = builder.batchUpdateId;
		this.lastUpdated = builder.lastUpdated;
		this.modifiers = builder.modifiers;
		this.items = builder.items;
	}

	public ModifierList() {
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

	public String getBatchUpdateId() {
		return batchUpdateId;
	}

	public void setBatchUpdateId(String batchUpdateId) {
		this.batchUpdateId = batchUpdateId;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public List<Modifier> getModifiers() {
		return this.modifiers;
	}

	public void setModifiers(List<Modifier> modifiers) {
		this.modifiers = modifiers;
	}

	public Set<Item> getItems() {
		return this.items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}


	@Override
	public String toString() {
		return "{" +
			" id='" + id + "'" +
			", name='" + name + "'" +
			", selectionType='" + selectionType + "'" +
			", image='" + image + "'" +
			", batchUpdateId='" + batchUpdateId + "'" +
			", lastUpdated='" + lastUpdated + "'" +
			", modifiers='" + modifiers + "'" +
			", items='" + items + "'" +
			"}";
	}
}
