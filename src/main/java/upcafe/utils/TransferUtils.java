package upcafe.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import upcafe.dto.catalog.ImageDTO;
import upcafe.dto.catalog.ModifierDTO;
import upcafe.dto.catalog.ModifierListDTO;
import upcafe.entity.catalog.Image;
import upcafe.entity.catalog.Modifier;
import upcafe.entity.catalog.ModifierList;

public class TransferUtils {

	public static ImageDTO toImageDTO(Image image) {
		ImageDTO imageDTO = null;

		if (image != null) {

			imageDTO = new ImageDTO.Builder().name(image.getName()).caption(image.getCaption()).url(image.getUrl())
					.build();
		}

		return imageDTO;
	}

	public static List<ModifierDTO> toModifierDTOs(List<Modifier> modifiersDB) {
		List<ModifierDTO> modifiersDTO = new ArrayList<ModifierDTO>();

		modifiersDB.forEach(modifierDB -> {
			ModifierDTO modifierDTO = new ModifierDTO.Builder(modifierDB.getId()).inStock(modifierDB.isInStock())
					.modifierListId(modifierDB.getModifierList().getId()).price(modifierDB.getPrice())
					.name(modifierDB.getName()).onByDefault(modifierDB.isOnByDefault())
					.image(toImageDTO(modifierDB.getImage())).build();
			modifiersDTO.add(modifierDTO);
		});

		return modifiersDTO;
	}
	
    public static Set<ModifierListDTO> toModifierListDTOs(Set<ModifierList> modifierListsDB) {
        Set<ModifierListDTO> modifierListDTOs = new HashSet<ModifierListDTO>();

        modifierListsDB.forEach(modifierListDB -> {


            ModifierListDTO modifierListDTO = new ModifierListDTO.Builder(modifierListDB.getId())
                    .name(modifierListDB.getName())
                    .selectionType(modifierListDB.getSelectionType())
                    .image(TransferUtils.toImageDTO(modifierListDB.getImage()))
                    .modifiers(toModifierDTOs(modifierListDB.getModifiers()))
                    .build();

            modifierListDTOs.add(modifierListDTO);

        });

        return modifierListDTOs;
    }
}
