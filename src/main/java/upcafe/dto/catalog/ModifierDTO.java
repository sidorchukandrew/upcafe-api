package upcafe.dto.catalog;

import upcafe.entity.catalog.Image;

public class ModifierDTO {

    private String id;

    private double price;

    private String name;

    private boolean onByDefault;

    private boolean inStock;

    private Image image;

    private String modifierListId;

    public ModifierDTO(String id, double price, String name, boolean onByDefault, boolean inStock, Image image,
            String modifierListId) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.onByDefault = onByDefault;
        this.inStock = inStock;
        this.image = image;
        this.modifierListId = modifierListId;
    }

    public ModifierDTO(String id, double price, String name, boolean onByDefault, boolean inStock, Image image) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.onByDefault = onByDefault;
        this.inStock = inStock;
        this.image = image;
    }

    public ModifierDTO() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOnByDefault() {
        return this.onByDefault;
    }

    public boolean getOnByDefault() {
        return this.onByDefault;
    }

    public void setOnByDefault(boolean onByDefault) {
        this.onByDefault = onByDefault;
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

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getModifierListId() {
        return this.modifierListId;
    }

    public void setModifierListId(String modifierListId) {
        this.modifierListId = modifierListId;
    }

    @Override
    public String toString() {
        return "{" + " id='" + getId() + "'" + ", price='" + getPrice() + "'" + ", name='" + getName() + "'"
                + ", onByDefault='" + isOnByDefault() + "'" + ", inStock='" + isInStock() + "'" + ", image='"
                + getImage() + "'" + ", modifierListId='" + getModifierListId() + "'" + "}";
    }

}