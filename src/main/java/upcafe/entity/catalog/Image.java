package upcafe.entity.catalog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Image {

	@Id
	@Column(name = "id")
	private String imageId;
	private String name;
	private String url;
	private String caption;
	private String batchUpdateId;
	private String updatedAt;

	public Image(String imageId, String name, String url, String caption, String batchUpdateId, String updatedAt) {
		super();
		this.imageId = imageId;
		this.name = name;
		this.url = url;
		this.caption = caption;
		this.batchUpdateId = batchUpdateId;
		this.updatedAt = updatedAt;
	}

	public Image() {

	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		return "Image [imageId=" + imageId + ", name=" + name + ", url=" + url + ", caption=" + caption
				+ ", batchUpdateId=" + batchUpdateId + ", updatedAt=" + updatedAt + "]";
	}
}
