package upcafe.entity.catalog;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Image {

	@Id
	@Column(name = "id", length = 36)
	private String id;
	private String name;
	private String url;
	private String caption;

	@Column(length = 36)
	private String batchUpdateId;

	private LocalDateTime updatedAt;

	public Image(String imageId, String name, String url, String caption, String batchUpdateId,
			LocalDateTime updatedAt) {
		super();
		this.id = imageId;
		this.name = name;
		this.url = url;
		this.caption = caption;
		this.batchUpdateId = batchUpdateId;
		this.updatedAt = updatedAt;
	}

	public Image() {

	}

	public String getId() {
		return id;
	}

	public void setId(String imageId) {
		this.id = imageId;
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

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "Image [imageId=" + id + ", name=" + name + ", url=" + url + ", caption=" + caption + ", batchUpdateId="
				+ batchUpdateId + ", updatedAt=" + updatedAt + "]";
	}
}
