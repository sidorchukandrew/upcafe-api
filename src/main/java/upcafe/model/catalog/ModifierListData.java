package upcafe.model.catalog;

import java.util.ArrayList;
import java.util.List;

public class ModifierListData {

	private String nameOfList;
	private String selectionType;
	private String imageUrl;
	private List<ModifierData> modifiers;
	
	public ModifierListData() {
		modifiers = new ArrayList<ModifierData>();
	}

	public String getNameOfList() {
		return nameOfList;
	}
	
	public void setNameOfList(String nameOfList) {
		this.nameOfList = nameOfList;
	}
	
	public String getSelectionType() {
		return selectionType;
	}
	
	public void setSelectionType(String selectionType) {
		this.selectionType = selectionType;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public void setImageUrl(String modifierListImageUrl) {
		this.imageUrl = modifierListImageUrl;
	}

	public List<ModifierData> getModifiers() {
		return modifiers;
	}

	public void setModifiers(List<ModifierData> modifiers) {
		this.modifiers = modifiers;
	}

	@Override
	public String toString() {
		return "\n\t\tModifierListData [nameOfList=" + nameOfList + ", selectionType=" + selectionType + ", imageUrl="
				+ imageUrl + ", modifiers=" + modifiers + "]";
	}

}
