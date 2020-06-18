package upcafe.dto.menu;

import java.util.List;

public class MenuDTO {

	private List<CategoryDTO> categories;
	
	public MenuDTO() { }
	
	public static class Builder {
		private List<CategoryDTO> categories;
		
		public Builder categories(List<CategoryDTO> categories) {
			this.categories = categories;
			return this;
		}
		
		public MenuDTO build() {
			return new MenuDTO(this);
		}
	}
	
	private MenuDTO(Builder builder) {
		this.categories = builder.categories;
	}

	public List<CategoryDTO> getCategories() {
		return categories;
	}

	@Override
	public String toString() {
		return "MenuDTO [categories=" + categories + "]";
	}
}
