package upcafe.dto.catalog;

import java.util.Set;

public class CatalogInventoryUpdate {

    private Set<VariationDTO> variations;
    private Set<ModifierDTO> modifiers;

    public CatalogInventoryUpdate() {
    }

    public static class Builder {
        Set<VariationDTO> variations;
        Set<ModifierDTO> modifiers;

        public Builder items(Set<VariationDTO> variations) {
            this.variations = variations;
            return this;
        }

        public Builder modifiers(Set<ModifierDTO> modifiers) {
            this.modifiers = modifiers;
            return this;
        }

        public CatalogInventoryUpdate build() {
            return new CatalogInventoryUpdate(this);
        }
    }

    private CatalogInventoryUpdate(Builder builder) {
        this.variations = builder.variations;
        this.modifiers = builder.modifiers;
    }

    public Set<ModifierDTO> getModifiers() {
        return modifiers;
    }

    public Set<VariationDTO> getVariations() {
        return variations;
    }

    @Override
    public String toString() {
        return "CatalogInventoryUpdate{" +
                "variations=" + variations +
                ", modifiers=" + modifiers +
                '}';
    }
}
