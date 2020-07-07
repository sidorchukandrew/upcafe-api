package upcafe.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import upcafe.dto.catalog.ImageDTO;
import upcafe.dto.catalog.ModifierDTO;
import upcafe.dto.catalog.ModifierListDTO;
import upcafe.dto.users.UserDTO;
import upcafe.entity.catalog.Image;
import upcafe.entity.catalog.Modifier;
import upcafe.entity.catalog.ModifierList;
import upcafe.entity.signin.User;
import upcafe.error.MissingParameterException;

public class TransferUtils {

	
	public static User toUserEntity(UserDTO userDTO) {
		if(userDTO == null) return null;
		
			return new User.Builder(userDTO.getEmail())
					.id(userDTO.getId())
					.build();
	}
	
	public static UserDTO toUserDTO(User user) {
		if(user == null) return null;
		
		if(user.getRoles() == null) {
			return new UserDTO.Builder(user.getEmail())
			.id(user.getId())
			.imageUrl(user.getImageUrl())
			.name(user.getName())
			.build();
		}
		return new UserDTO.Builder(user.getEmail())
				.id(user.getId())
				.imageUrl(user.getImageUrl())
				.name(user.getName())
				.roles(user.getRoles().stream().map(role -> role.getAuthority()).collect(Collectors.toSet()))
				.build();
	}
	
	public static ImageDTO toImageDTO(Image image) {

		ImageDTO imageDTO = null;

		if (image != null) {

			if (image.getUrl() == null || image.getUrl().compareTo("") == 0)
				throw new MissingParameterException("url");

			imageDTO = new ImageDTO.Builder().name(image.getName()).caption(image.getCaption()).url(image.getUrl())
					.build();
		}

		return imageDTO;
	}

	public static ModifierDTO toModifierDTO(Modifier modifierDB) {

		ModifierDTO modifierDTO = null;

		if (modifierDB != null) {

			if (modifierDB.getId() == null || modifierDB.getId().compareTo("") == 0)
				throw new MissingParameterException("id");

			modifierDTO = new ModifierDTO.Builder(modifierDB.getId()).inStock(modifierDB.isInStock())
					.modifierListId(modifierDB.getModifierList().getId()).price(modifierDB.getPrice())
					.name(modifierDB.getName()).onByDefault(modifierDB.isOnByDefault())
					.image(toImageDTO(modifierDB.getImage())).build();
		}

		return modifierDTO;
	}

	public static List<ModifierDTO> toModifierDTOs(List<Modifier> modifiersDB) {
		
		if(modifiersDB == null)
			return null;
		
		List<ModifierDTO> modifiersDTO = new ArrayList<ModifierDTO>();
		modifiersDB.forEach(modifierDB -> modifiersDTO.add(toModifierDTO(modifierDB)));

		return modifiersDTO;
	}

	public static Set<ModifierListDTO> toModifierListDTOs(Set<ModifierList> modifierListsDB) {

		if (modifierListsDB != null) {
			Set<ModifierListDTO> modifierListDTOs = new HashSet<>();
			modifierListsDB.forEach(modifierListDB -> modifierListDTOs.add(toModifierListDTO(modifierListDB)));
			
			return modifierListDTOs;
		}

		return null;
	}

	public static ModifierListDTO toModifierListDTO(ModifierList modifierListDB) {

		ModifierListDTO modifierListDTO = null;

		if (modifierListDB != null) {

			if (modifierListDB.getId() == null || modifierListDB.getId().compareTo("") == 0)
				throw new MissingParameterException("id");

			modifierListDTO = new ModifierListDTO.Builder(modifierListDB.getId()).name(modifierListDB.getName())
					.selectionType(modifierListDB.getSelectionType()).image(toImageDTO(modifierListDB.getImage()))
					.modifiers(toModifierDTOs(modifierListDB.getModifiers())).build();
		}

		return modifierListDTO;
	}
}
