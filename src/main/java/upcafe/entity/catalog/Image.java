package upcafe.entity.catalog;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

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

	private LocalDateTime lastUpdated;

	public Image(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.url = builder.url;
		this.caption = builder.caption;
		this.batchUpdateId = builder.batchUpdateId;
		this.lastUpdated = builder.lastUpdated;
	}

	public static class Builder {
		private final String id;
		private String name;
		private String url;
		private String caption;
		private String batchUpdateId;
		private LocalDateTime lastUpdated;

		public Builder(String id) {
			this.id = id;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder url(String url) {
			this.url = url;
			return this;
		}

		public Builder caption(String caption) {
			this.caption = caption;
			return this;
		}

		public Builder batchUpdateId(String batchUpdateId) {
			this.batchUpdateId = batchUpdateId;
			return this;
		}

		public Builder lastUpdated(LocalDateTime lastUpdated) {
			this.lastUpdated = lastUpdated;
			return this;
		}

		public Image build() {
			return new Image(this);
		}
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

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	@Override
	public String toString() {
		return "Image [imageId=" + id + ", name=" + name + ", url=" + url + ", caption=" + caption + ", batchUpdateId="
				+ batchUpdateId + ", updatedAt=" + lastUpdated + "]";
	}
}
