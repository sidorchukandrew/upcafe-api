package upcafe.entity.catalog;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Modifier {

    @Id
    @Column(length = 36)
    private String id;

    @Column(precision = 4)
    private double price;

    @Column(length = 36)
    private String name;

    @ManyToOne
    @JoinColumn(name = "list_id", referencedColumnName = "id")
    private ModifierList modList;

    private boolean onByDefault;

    private boolean inStock;

    @Column(length = 36)
    private String batchUpdateId;

    private LocalDateTime lastUpdated;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    public static class Builder {

        private final String id;
        private String name;
        private double price;
        private ModifierList modList;
        private boolean onByDefault;
        private LocalDateTime lastUpdated;
        private boolean inStock;
        private String batchUpdateId;
        private Image image;

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

        public Builder modifierList(ModifierList modList) {
            this.modList = modList;
            return this;
        }

        public Builder onByDefault(boolean onByDefault) {
            this.onByDefault = onByDefault;
            return this;
        }

        public Builder lastUpdated(LocalDateTime lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        public Builder inStock(boolean inStock) {
            this.inStock = inStock;
            return this;
        }

        public Builder batchUpdateId(String batchUpdateId) {
            this.batchUpdateId = batchUpdateId;
            return this;
        }

        public Builder image(Image image) {
            this.image = image;
            return this;
        }


        public Modifier build() {
            return new Modifier(this);
        }
    }

    private Modifier(Builder builder) {
        this.id = builder.id;
        this.price = builder.price;
        this.modList = builder.modList;
        this.onByDefault = builder.onByDefault;
        this.name = builder.name;
        this.batchUpdateId = builder.batchUpdateId;
        this.lastUpdated = builder.lastUpdated;
        this.inStock = builder.inStock;
        this.image = builder.image;
    }

    public Modifier() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ModifierList getModifierList() {
        return modList;
    }

    public void setModifierList(ModifierList modList) {
        this.modList = modList;
    }

    public boolean isOnByDefault() {
        return onByDefault;
    }

    public void setOnByDefault(boolean onByDefault) {
        this.onByDefault = onByDefault;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
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

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public void setModList(ModifierList modList) {
        this.modList = modList;
    }

    public ModifierList getModList() {
        return modList;
    }

    @Override
    public String toString() {
        return "{" + " id='" + getId() + "'" + ", price='" + getPrice() + "'" + ", name='" + getName() + "'"
                + ", modList='" + getModList() + "'" + ", onByDefault='" + isOnByDefault() + "'" + ", inStock='"
                + isInStock() + "'" + ", batchUpdateId='" + getBatchUpdateId() + "'" + ", updatedAt='" + getLastUpdated()
                + "'" + ", image='" + getImage() + "'" + "}";
    }

}
