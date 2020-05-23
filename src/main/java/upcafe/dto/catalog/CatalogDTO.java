package upcafe.dto.catalog;

import java.util.List;
import java.util.Set;

public class CatalogDTO {

    private Set<ModifierListDTO> modifierLists;
    private List<VariationDTO> itemsList;

    public static class Builder {
        private Set<ModifierListDTO> modifierLists;
        private List<VariationDTO> itemsList;

        public Builder modifierLists(Set<ModifierListDTO> modifierLists) {
            this.modifierLists = modifierLists;
            return this;
        }

        public Builder itemsList(List<VariationDTO> itemsList) {
            this.itemsList = itemsList;
            return this;
        }

        public CatalogDTO build() {
            return new CatalogDTO(this);
        }
    }

    private CatalogDTO(Builder builder) {
        this.itemsList = builder.itemsList;
        this.modifierLists = builder.modifierLists;
    }

    public Set<ModifierListDTO> getModifierLists() {
        return modifierLists;
    }

    public List<VariationDTO> getItemsList() {
        return itemsList;
    }

    @Override
    public String toString() {
        return "CatalogDTO{" +
                "modifierLists=" + modifierLists +
                ", itemsList=" + itemsList +
                '}';
    }
}
