package upcafe.model.catalog;

import java.util.ArrayList;
import java.util.List;

import upcafe.entity.catalog.Modifier;

public class CategoryItem {

	private ItemData itemData;
	private List<VariationData> variationsData;
	private List<ModifierListData> modifierListsData;
	
	public CategoryItem() {
		variationsData = new ArrayList<VariationData>();
		modifierListsData = new ArrayList<ModifierListData>();
	}
	
	public ItemData getItemData() {
		return itemData;
	}
	
	public void setItemData(ItemData itemData) {
		this.itemData = itemData;
	}
	
	public List<ModifierListData> getModifierListsData() {
		return modifierListsData;
	}
	
	public void setModifierListsData(List<ModifierListData> modifierListsData) {
		this.modifierListsData = modifierListsData;
	}

	public List<VariationData> getVariationsData() {
		return variationsData;
	}

	public void setVariationsData(List<VariationData> variationsData) {
		this.variationsData = variationsData;
	}

	@Override
	public String toString() {
		return "\nCategoryItem [itemData=" + itemData + ", variationsData=" + variationsData + ", modifierListsData="
				+ modifierListsData + "]";
	}

}
