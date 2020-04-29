package upcafe.entity.catalog;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

// public class ItemModListId implements Serializable {
// 	String itemId;
// 	String modifierListId;
// 	String batchUpdateId;
// }

@Embeddable
public class ItemModListKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "item_id", length = 36)
	String itemId;

	@Column(name = "modifier_list_id", length = 36)
	String modifierListId;

	@Column(name = "batch_update_id", length = 36)
	String batchUpdateId;

	public ItemModListKey(String itemId, String modifierListId, String batchUpdateId) {
		this.itemId = itemId;
		this.modifierListId = modifierListId;
		this.batchUpdateId = batchUpdateId;
	}

	public ItemModListKey() {

	}

	public String getItemId() {
		return this.itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getModifierListId() {
		return this.modifierListId;
	}

	public void setModifierListId(String modifierListId) {
		this.modifierListId = modifierListId;
	}

	public String getBatchUpdateId() {
		return this.batchUpdateId;
	}

	public void setBatchUpdateId(String batchUpdateId) {
		this.batchUpdateId = batchUpdateId;
	}

	@Override
	public String toString() {
		return "{" + " itemId='" + getItemId() + "'" + ", modifierListId='" + getModifierListId() + "'"
				+ ", batchUpdateId='" + getBatchUpdateId() + "'" + "}";
	}

}
