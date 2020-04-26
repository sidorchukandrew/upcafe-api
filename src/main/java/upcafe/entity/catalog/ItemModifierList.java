package upcafe.entity.catalog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

//@Entity
//@IdClass(ItemModListId.class)
public class ItemModifierList {

	@Id
	@Column(name = "item_id")
	private String itemId; // THIS_ITEM_ID_111
	@Id
	@Column(name = "mod_list_id")
	private String modifierListId; // A_MODIFIER_LIST_THAT_CAN_BE_APPLIED_TO_THIS_ITEM
	@Id
	@Column(name = "batch_update_id")
	private String batchUpdateId;
	private String updatedAt;

	public ItemModifierList(String itemId, String modifierListId, String batchUpdateId, String updatedAt) {
		super();
		this.itemId = itemId;
		this.modifierListId = modifierListId;
		this.batchUpdateId = batchUpdateId;
		this.updatedAt = updatedAt;
	}

	public ItemModifierList() { }

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getModifierListId() {
		return modifierListId;
	}

	public void setModifierList(String modifierListId) {
		this.modifierListId = modifierListId;
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
		return "ItemModifierList [itemId=" + itemId + ", modifierListId=" + modifierListId + ", batchUpdateId="
				+ batchUpdateId + ", updatedAt=" + updatedAt + "]";
	}
}
