package upcafe.dto.catalog;

public class VariationDTO {
    
    private String id;
    private String name;
    private double price;
    private boolean inStock;
    private ImageDTO image;

    public static class Builder {
        private final String id;
        private String name;
        private double price;
        private boolean inStock;
        private ImageDTO image;

        public Builder(String id) {
            this.id = id;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(double price) {
            this.price = price;
            return this;
        }

        public Builder inStock(boolean inStock) {
            this.inStock = inStock;
            return this;
        }

        public Builder image(ImageDTO image) {
            this.image = image;
            return this;
        }

        public VariationDTO build() {
            return new VariationDTO(this);
        }
    }

    private VariationDTO(Builder builder) {
        id = builder.id;
        name = builder.name;
        image = builder.image;
        inStock = builder.inStock;
        price = builder.price;
    }


    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    public boolean getInStock() {
        return this.inStock;
    }

    public boolean isInStock() {
        return this.inStock;
    }

    public ImageDTO getImage() {
        return this.image;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + id + "'" +
            ", name='" + name + "'" +
            ", price='" + price + "'" +
            ", inStock='" + inStock + "'" +
            ", image='" + image + "'" +
            "}";
    }
}