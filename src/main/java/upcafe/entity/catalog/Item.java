package upcafe.entity.catalog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Item {

    @Id
    @Column(length = 36)
    private String id;

    @Column(length = 52)
    private String name;
    private String description;

    @Column(length = 36)
    private String batchUpdateId;

    private LocalDateTime lastUpdated;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    private Category category;

    @OneToMany(mappedBy = "item")
    private List<Variation> variations;

    // @OneToMany(mappedBy = "item")
    // private List<ItemModifierList> modifierLists;

    @ManyToMany
    Set<ModifierList> modifierLists;

    public static class Builder {
        private final String id;
        private String name;
        private String batchUpdateId;
        private String description = "No description set yet";
        private LocalDateTime lastUpdated;
        private Category category;
        private List<Variation> variations;
        private Set<ModifierList> modifierLists;

        public Builder(String id) {
            this.id = id;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder batchUpdateId(String batchUpdateId) {
            this.batchUpdateId = batchUpdateId;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder lastUpdated(LocalDateTime lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        public Builder category(Category category) {
            this.category = category;
            return this;
        }

        public Builder variations(List<Variation> variations) {
            this.variations = variations;
            return this;
        }

        public Builder modifierLists(Set<ModifierList> modifierLists) {
            this.modifierLists = modifierLists;
            return this;
        }

        public Item build() {
            return new Item(this);
        }
    }

    public Item() {
    }

    private Item(Builder builder) {
        this.batchUpdateId = builder.batchUpdateId;
        this.category = builder.category;
        this.description = builder.description;
        this.id = builder.id;
        this.lastUpdated = builder.lastUpdated;
        this.modifierLists = builder.modifierLists;
        this.variations = builder.variations;
        this.name = builder.name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public void setLastUpdated(LocalDateTime updatedAt) {
        this.lastUpdated = updatedAt;
    }

    public void setModifierLists(Set<ModifierList> modifierLists) {
        this.modifierLists = modifierLists;
    }

    public Set<ModifierList> getModifierLists() {
        return modifierLists;
    }

    public List<Variation> getVariations() {
        return variations;
    }

    public void setVariations(List<Variation> variations) {
        this.variations = variations;
    }

    @Override
    public String toString() {
        return "{" + " id='" + getId() + "'" + ", name='" + getName() + "'" + ", description='" + getDescription() + "'"
                + ", batchUpdateId='" + getBatchUpdateId() + "'" + ", updatedAt='" + getLastUpdated() + "'"
                + ", category='" + getCategory() + "'" + ", variations='" + getVariations() + "'" + ", modifierLists='"
                + getModifierLists() + "'" + "}";
    }

}
