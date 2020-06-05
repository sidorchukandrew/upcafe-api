package upcafe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import upcafe.dto.catalog.ImageDTO;
import upcafe.dto.catalog.MenuItemDTO;
import upcafe.dto.catalog.ModifierDTO;
import upcafe.dto.catalog.ModifierListDTO;
import upcafe.entity.catalog.Image;
import upcafe.entity.catalog.Modifier;
import upcafe.entity.catalog.ModifierList;
import upcafe.entity.catalog.Variation;
import upcafe.repository.catalog.VariationRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class MenuService {

    @Autowired
    private VariationRepository variationRepository;

    public MenuItemDTO getMenuItemById(String id) {
        Variation variation = variationRepository.getOne(id);

        String nameOfMenuItem = (variation.getName().compareTo("Regular") == 0)
                ? variation.getItem().getName()
                : variation.getName();

        return new MenuItemDTO.Builder(variation.getId())
                .description(variation.getItem().getDescription())
                .image(transferToImageDTO(variation.getImage()))
                .modifierLists(transferToListOfModifierListDTOs(variation.getItem().getModifierLists()))
                .price(variation.getPrice())
                .inStock(variation.getInStock())
                .name(nameOfMenuItem)
                .build();
    }

    private Set<ModifierListDTO> transferToListOfModifierListDTOs(Set<ModifierList> modifierListsDB) {
        Set<ModifierListDTO> modifierListDTOs = new HashSet<ModifierListDTO>();

        modifierListsDB.forEach(modifierListDB -> {


            ModifierListDTO modifierListDTO = new ModifierListDTO.Builder(modifierListDB.getId())
                    .name(modifierListDB.getName())
                    .selectionType(modifierListDB.getSelectionType())
                    .image(transferToImageDTO(modifierListDB.getImage()))
                    .modifiers(transferToListOfModifierDTOs(modifierListDB.getModifiers()))
                    .build();

            modifierListDTOs.add(modifierListDTO);

        });

        return modifierListDTOs;
    }

    private ImageDTO transferToImageDTO(Image image) {
        ImageDTO imageDTO = null;

        if (image != null) {

            imageDTO = new ImageDTO.Builder()
                    .name(image.getName())
                    .caption(image.getCaption())
                    .url(image.getUrl())
                    .build();
        }

        return imageDTO;
    }

    private List<ModifierDTO> transferToListOfModifierDTOs(List<Modifier> modifiersDB) {
        List<ModifierDTO> modifiersDTO = new ArrayList<ModifierDTO>();

        modifiersDB.forEach(modifierDB -> {
            ModifierDTO modifierDTO = new ModifierDTO.Builder(modifierDB.getId())
                    .inStock(modifierDB.isInStock())
                    .modifierListId(modifierDB.getModifierList().getId())
                    .price(modifierDB.getPrice())
                    .name(modifierDB.getName())
                    .onByDefault(modifierDB.isOnByDefault())
                    .image(transferToImageDTO(modifierDB.getImage()))
                    .build();
            modifiersDTO.add(modifierDTO);
        });

        return modifiersDTO;
    }

}
