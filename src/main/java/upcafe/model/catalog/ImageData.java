package upcafe.model.catalog;

public class ImageData {
	
	private String url;
	private String caption;
	private String catalogObjectId;
	private String name;
	
	public ImageData(String url, String caption, String catalogObjectId, String name) {
		super();
		this.url = url;
		this.caption = caption;
		this.catalogObjectId = catalogObjectId;
		this.name = name;
	}
	
	public ImageData() {
		super();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getCatalogObjectId() {
		return catalogObjectId;
	}

	public void setCatalogObjectId(String catalogObjectId) {
		this.catalogObjectId = catalogObjectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ImageData [url=" + url + ", caption=" + caption + ", catalogObjectId=" + catalogObjectId + ", name="
				+ name + "]";
	}
}
