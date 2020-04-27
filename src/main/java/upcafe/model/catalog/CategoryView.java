package upcafe.model.catalog;

public class CategoryView {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "CategoryView [name=" + name + "]";
	}

	public CategoryView(String name) {
		super();
		this.name = name;
	}

	public CategoryView() {
		super();
	}

}
