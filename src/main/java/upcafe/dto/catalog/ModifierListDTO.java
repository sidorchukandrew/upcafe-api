package upcafe.dto.catalog;

import java.util.List;

import upcafe.entity.catalog.Image;

public class ModifierListDTO {

    private String id;
    private String name;
    private String selectionType;
    private Image image;
    private List<ModifierDTO> modifiers;

    public ModifierListDTO(String id, String name, String selectionType, Image image, List<ModifierDTO> modifiers) {
        this.id = id;
        this.name = name;
        this.selectionType = selectionType;
        this.image = image;
        this.modifiers = modifiers;
    }

    public ModifierListDTO() {

    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelectionType() {
        return this.selectionType;
    }

    public void setSelectionType(String selectionType) {
        this.selectionType = selectionType;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<ModifierDTO> getModifiers() {
        return this.modifiers;
    }

    public void setModifiers(List<ModifierDTO> modifiers) {
        this.modifiers = modifiers;
    }

    @Override
    public String toString() {
        return "{" + " id='" + getId() + "'" + ", name='" + getName() + "'" + ", selectionType='" + getSelectionType()
                + "'" + ", image='" + getImage() + "'" + ", modifiers='" + getModifiers() + "'" + "}";
    }

}