package upcafe.dto.catalog;

public class ModifierDTO {

    private String id;
    private double price;
    private String name;
    private boolean onByDefault;
    private boolean inStock;
    private ImageDTO image;
    private String modifierListId;

    public static class Builder {
        private final String id;
        private double price;
        private String name;
        private boolean onByDefault;
        private boolean inStock;
        private ImageDTO image;
        private String modifierListId;

        public Builder(String id) {
            this.id = id;
        }

        public Builder price(double price) {
            this.price = price;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder onByDefault(boolean onByDefault) {
            this.onByDefault = onByDefault;
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

        public Builder modifierListId(String id) {
            this.modifierListId = id;
            return this;
        }

        public ModifierDTO build() {
            return new ModifierDTO(this);
        }
    }

    public ModifierDTO(Builder builder) {
        this.id = builder.id;
        this.price = builder.price;
        this.name = builder.name;
        this.onByDefault = builder.onByDefault;
        this.inStock = builder.inStock;
        this.image = builder.image;
        this.modifierListId = builder.modifierListId;
    }

    public ModifierDTO() {
    }


    public String getId() {
        return this.id;
    }

    public double getPrice() {
        return this.price;
    }

    public String getName() {
        return this.name;
    }

    public boolean getOnByDefault() {
        return this.onByDefault;
    }

    public boolean isOnByDefault() {
        return this.onByDefault;
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

    public String getModifierListId() {
        return this.modifierListId;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + id + "'" +
                ", price='" + price + "'" +
                ", name='" + name + "'" +
                ", onByDefault='" + onByDefault + "'" +
                ", inStock='" + inStock + "'" +
                ", image='" + image + "'" +
                ", modifierListId='" + modifierListId + "'" +
                "}";
    }


}