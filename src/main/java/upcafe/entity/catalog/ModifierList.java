package upcafe.entity.catalog;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

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

	@JsonFormat(pattern = "EEE MMM dd yyyy HH:mm:ss")
	private LocalDateTime updatedAt;

	@OneToMany(mappedBy = "modList")
	private List<Modifier> modifiers;

	public ModifierList(String id, String name, String selectionType, Image image, String batchUpdateId,
			LocalDateTime updatedAt, List<Modifier> modifiers) {
		this.id = id;
		this.name = name;
		this.selectionType = selectionType;
		this.image = image;
		this.batchUpdateId = batchUpdateId;
		this.updatedAt = updatedAt;
		this.modifiers = modifiers;
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

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<Modifier> getModifiers() {
		return this.modifiers;
	}

	public void setModifiers(List<Modifier> modifiers) {
		this.modifiers = modifiers;
	}

	@Override
	public String toString() {
		return "{" + " id='" + getId() + "'" + ", name='" + getName() + "'" + ", selectionType='" + getSelectionType()
				+ "'" + ", image='" + getImage() + "'" + ", batchUpdateId='" + getBatchUpdateId() + "'"
				+ ", updatedAt='" + getUpdatedAt() + "'" + ", modifiers='" + getModifiers() + "'" + "}";
	}

}
