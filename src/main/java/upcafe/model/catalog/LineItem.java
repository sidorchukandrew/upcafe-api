package upcafe.model.catalog;

import java.util.List;

public class LineItem {
	
	private ItemData itemData;
	private VariationData variationData;
	private List<ModifierListData> modifierListsData;
	
	public LineItem(ItemData itemData, VariationData variationData, List<ModifierListData> modifierListsData) {
		super();
		this.itemData = itemData;
		this.variationData = variationData;
		this.modifierListsData = modifierListsData;
	}
	
	public LineItem() { }

	public ItemData getItemData() {
		return itemData;
	}

	public void setItemData(ItemData itemData) {
		this.itemData = itemData;
	}

	public VariationData getVariationData() {
		return variationData;
	}

	public void setVariationData(VariationData variationData) {
		this.variationData = variationData;
	}

	public List<ModifierListData> getModifierListsData() {
		return modifierListsData;
	}

	public void setModifierListsData(List<ModifierListData> modifierListsData) {
		this.modifierListsData = modifierListsData;
	}

	@Override
	public String toString() {
		return "LineItem [itemData=" + itemData + ", variationData=" + variationData + ", modifierListsData="
				+ modifierListsData + "]";
	}
}
