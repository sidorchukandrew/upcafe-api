package upcafe.dto.menu;

import java.util.List;
import java.util.Set;

import upcafe.dto.catalog.ImageDTO;
import upcafe.dto.catalog.ModifierListDTO;

public class MenuItemDTO {
    private String id;
    private Set<ModifierListDTO> modifierLists;
    private String name;
    private String description;
    private double price;
    private ImageDTO image;
    private boolean inStock;

    public static class Builder {
        private final String id;
        private Set<ModifierListDTO> modifierLists;
        private String name;
        private String description;
        private double price;
        private ImageDTO image;
        private boolean inStock;

        public Builder(String id) {
            this.id = id;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder image(ImageDTO image) {
            this.image = image;
            return this;
        }

        public Builder inStock(boolean inStock) {
            this.inStock = inStock;
            return this;
        }

        public Builder price(double price) {
            this.price = price;
            return this;
        }

        public Builder modifierLists(Set<ModifierListDTO> modifierLists) {
            this.modifierLists = modifierLists;
            return this;
        }

        public MenuItemDTO build() {
            return new MenuItemDTO(this);
        }
    }

    private MenuItemDTO(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.image = builder.image;
        this.inStock = builder.inStock;
        this.price = builder.price;
        this.modifierLists = builder.modifierLists;
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<ModifierListDTO> getModifierLists() {
        return this.modifierLists;
    }

    public void setModifierLists(Set<ModifierListDTO> modifierLists) {
        this.modifierLists = modifierLists;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ImageDTO getImage() {
        return this.image;
    }

    public void setImage(ImageDTO image) {
        this.image = image;
    }

    public boolean isInStock() {
        return this.inStock;
    }

    public boolean getInStock() {
        return this.inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + id + "'" +
                ", modifierLists='" + modifierLists + "'" +
                ", name='" + name + "'" +
                ", description='" + description + "'" +
                ", price='" + price + "'" +
                ", image='" + image + "'" +
                ", inStock='" + inStock + "'" +
                "}";
    }
}