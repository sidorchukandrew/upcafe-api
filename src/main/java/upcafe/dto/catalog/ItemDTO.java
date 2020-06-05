package upcafe.dto.catalog;

import java.util.List;

public class ItemDTO {

    private String id;
    private String name;
    private String description;
    private List<VariationDTO> variations;
    private List<ModifierListDTO> modifierLists;

    public static class Builder {
        private final String id;
        private String name;
        private String description;
        private List<VariationDTO> variations;
        private List<ModifierListDTO> modifierLists;

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

        public Builder modifierLists(List<ModifierListDTO> modifierLists) {
            this.modifierLists = modifierLists;
            return this;
        }

        public Builder variations(List<VariationDTO> variations) {
            this.variations = variations;
            return this;
        }

        public ItemDTO build() {
            return new ItemDTO(this);
        }
    }

    private ItemDTO(Builder builder) {
        id = builder.id;
        name = builder.name;
        variations = builder.variations;
        modifierLists = builder.modifierLists;
        description = builder.description;
    }


    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public List<VariationDTO> getVariations() {
        return this.variations;
    }

    public List<ModifierListDTO> getModifierLists() {
        return this.modifierLists;
    }


    @Override
    public String toString() {
        return "{" +
                " id='" + id + "'" +
                ", name='" + name + "'" +
                ", description='" + description + "'" +
                ", variations='" + variations + "'" +
                ", modifierLists='" + modifierLists + "'" +
                "}";
    }

}