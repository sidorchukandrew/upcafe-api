package upcafe.dto.menu;

import java.util.List;

public class CategoryDTO {

	private String name;
	private List<MenuItemDTO> items;
	
	public CategoryDTO() { }
	
	public static class Builder {
		private final String name;
		private List<MenuItemDTO> items;
		
		public Builder(String name) {
			this.name = name;
		}
		
		public Builder items(List<MenuItemDTO> items) {
			this.items = items;
			return this;
		}
		
		public CategoryDTO build() {
			return new CategoryDTO(this);
		}
	}
	
	private CategoryDTO(Builder builder) {
		this.items = builder.items;
		this.name = builder.name;
	}

	public String getName() {
		return name;
	}

	public List<MenuItemDTO> getItems() {
		return items;
	}

	@Override
	public String toString() {
		return "CategoryDTO [name=" + name + ", items=" + items + "]";
	}
}
