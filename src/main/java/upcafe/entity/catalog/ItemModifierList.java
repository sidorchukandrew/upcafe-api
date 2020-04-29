package upcafe.entity.catalog;

import java.time.LocalDateTime;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

//@Entity
//@IdClass(ItemModListId.class)
// public class ItemModifierList {

// 	@Id
// 	@Column(name = "item_id")
// 	private String itemId; // THIS_ITEM_ID_111
// 	@Id
// 	@Column(name = "mod_list_id")
// 	private String modifierListId; // A_MODIFIER_LIST_THAT_CAN_BE_APPLIED_TO_THIS_ITEM
// 	@Id
// 	@Column(name = "batch_update_id")
// 	private String batchUpdateId;
// 	private String updatedAt;

// 	public ItemModifierList(String itemId, String modifierListId, String batchUpdateId, String updatedAt) {
// 		super();
// 		this.itemId = itemId;
// 		this.modifierListId = modifierListId;
// 		this.batchUpdateId = batchUpdateId;
// 		this.updatedAt = updatedAt;
// 	}

// 	public ItemModifierList() { }

// 	public String getItemId() {
// 		return itemId;
// 	}

// 	public void setItemId(String itemId) {
// 		this.itemId = itemId;
// 	}

// 	public String getModifierListId() {
// 		return modifierListId;
// 	}

// 	public void setModifierList(String modifierListId) {
// 		this.modifierListId = modifierListId;
// 	}

// 	public String getBatchUpdateId() {
// 		return batchUpdateId;
// 	}

// 	public void setBatchUpdateId(String batchUpdateId) {
// 		this.batchUpdateId = batchUpdateId;
// 	}

// 	public String getUpdatedAt() {
// 		return updatedAt;
// 	}

// 	public void setUpdatedAt(String updatedAt) {
// 		this.updatedAt = updatedAt;
// 	}

// 	@Override
// 	public String toString() {
// 		return "ItemModifierList [itemId=" + itemId + ", modifierListId=" + modifierListId + ", batchUpdateId="
// 				+ batchUpdateId + ", updatedAt=" + updatedAt + "]";
// 	}
// }

@Entity
public class ItemModifierList {

	@EmbeddedId
	private ItemModListKey id;

	@ManyToOne
	@MapsId("item_id")
	@JoinColumn(name = "item_id")
	private Item item;

	@ManyToOne
	@MapsId("modifier_list_id")
	@JoinColumn(name = "modifier_list_id")
	private ModifierList modifierList;

	// @MapsId("batch_update_id")
	// private String batchUpdateId;

	private LocalDateTime updatedAt;

	public ItemModifierList(ItemModListKey id, Item item, ModifierList modifierList, LocalDateTime updatedAt) {
		this.id = id;
		this.item = item;
		this.modifierList = modifierList;
		// this.batchUpdateId = batchUpdateId;
		this.updatedAt = updatedAt;
	}

	public ItemModifierList() {

	}

	public ItemModListKey getId() {
		return this.id;
	}

	public void setId(ItemModListKey id) {
		this.id = id;
	}

	public Item getItem() {
		return this.item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public ModifierList getModifierList() {
		return this.modifierList;
	}

	public void setModifierList(ModifierList modifierList) {
		this.modifierList = modifierList;
	}

	// public String getBatchUpdateId() {
	// return this.batchUpdateId;
	// }

	// public void setBatchUpdateId(String batchUpdateId) {
	// this.batchUpdateId = batchUpdateId;
	// }

	public LocalDateTime getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "{" + " id='" + getId() + "'" + ", item='" + getItem() + "'" + ", modifierList='" + getModifierList()
				+ "'" + ", updatedAt='" + getUpdatedAt() + "'" + "}";
	}
}