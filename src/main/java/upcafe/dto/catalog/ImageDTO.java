package upcafe.dto.catalog;

public class ImageDTO {
    private String name;
    private String url;
    private String caption;

    public ImageDTO() {

    }

    public ImageDTO(String name, String url, String caption) {
        this.name = name;
        this.url = url;
        this.caption = caption;
    }    

    private ImageDTO(Builder builder) {
        name = builder.name;
        url = builder.url;
        caption = builder.caption;
    }

    public static class Builder {

        private String name;
        private String url;
        private String caption;

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

        public ImageDTO build() {
            return new ImageDTO(this);
        }
    }


    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
    }

    public String getCaption() {
        return this.caption;
    }

    @Override
    public String toString() {
        return "{" +
            ", name='" + getName() + "'" +
            ", url='" + getUrl() + "'" +
            ", caption='" + getCaption() + "'" +
            "}";
    }

}