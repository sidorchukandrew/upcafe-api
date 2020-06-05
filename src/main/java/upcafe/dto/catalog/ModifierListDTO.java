package upcafe.dto.catalog;

import java.util.List;


public class ModifierListDTO {

    private String id;
    private String name;
    private String selectionType;
    private ImageDTO image;
    private List<ModifierDTO> modifiers;

    public static class Builder {
        private final String id;
        private String name;
        private String selectionType;
        private ImageDTO image;
        private List<ModifierDTO> modifiers;

        public Builder(String id) {
            this.id = id;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder selectionType(String selectionType) {
            this.selectionType = selectionType;
            return this;
        }

        public Builder image(ImageDTO image) {
            this.image = image;
            return this;
        }

        public Builder modifiers(List<ModifierDTO> modifiers) {
            this.modifiers = modifiers;
            return this;
        }

        public ModifierListDTO build() {
            return new ModifierListDTO(this);
        }
    }

    public ModifierListDTO(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.selectionType = builder.selectionType;
        this.image = builder.image;
        this.modifiers = builder.modifiers;
    }

    public ModifierListDTO() {

    }


    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getSelectionType() {
        return this.selectionType;
    }

    public ImageDTO getImage() {
        return this.image;
    }

    public List<ModifierDTO> getModifiers() {
        return this.modifiers;
    }


    @Override
    public String toString() {
        return "{" +
                " id='" + id + "'" +
                ", name='" + name + "'" +
                ", selectionType='" + selectionType + "'" +
                ", image='" + image + "'" +
                ", modifiers='" + modifiers + "'" +
                "}";
    }
}