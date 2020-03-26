package upcafe.model.catalog;

import java.util.ArrayList;
import java.util.List;

import com.squareup.square.models.CatalogItem;

public class Catalog {
	
	private String category;
	private List<CategoryItem> items;
	
	public Catalog() {
		items = new ArrayList<CategoryItem>();
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<CategoryItem> getItems() {
		return items;
	}

	public void setItems(List<CategoryItem> items) {
		this.items = items;
	}
}
