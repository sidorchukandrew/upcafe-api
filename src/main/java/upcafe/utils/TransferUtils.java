package upcafe.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.squareup.square.models.Customer;
import com.squareup.square.models.OrderFulfillment;
import com.squareup.square.models.OrderFulfillmentRecipient;
import com.squareup.square.models.OrderMoneyAmounts;
import upcafe.dto.catalog.ImageDTO;
import upcafe.dto.catalog.ModifierDTO;
import upcafe.dto.catalog.ModifierListDTO;
import upcafe.dto.users.UserDTO;
import upcafe.entity.catalog.Image;
import upcafe.entity.catalog.Modifier;
import upcafe.entity.catalog.ModifierList;
import upcafe.entity.signin.User;
import upcafe.error.MissingParameterException;
import upcafe.v2.enums.PickupState;
import upcafe.v2.models.Costs;
import upcafe.v2.models.Order;
import upcafe.v2.models.OrderItem;
import upcafe.v2.models.PickupDetails;

public class TransferUtils {

	private final static int CURRENCY_DENOMINATOR_CONVERTER = 100;

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

	public static Order toOrderDTO(com.squareup.square.models.Order squareOrder) {
		return new Order.Builder(squareOrder.getId())
				.orderItems(new ArrayList<OrderItem>())
				.costs(toCosts(squareOrder.getNetAmounts()))
				.pickupDetails(toPickupDetails(squareOrder.getFulfillments().get(0)))
				.customer(toCustomer(squareOrder.getFulfillments().get(0).getPickupDetails().getRecipient()))
				.version(squareOrder.getVersion())
				.build();
	}

	public static upcafe.v2.models.Customer toCustomer(OrderFulfillmentRecipient recipient) {
		return new upcafe.v2.models.Customer.Builder()
				.email(recipient.getEmailAddress())
				.name(recipient.getDisplayName())
				.phoneNumber(recipient.getPhoneNumber())
				.squareCustomerId(recipient.getCustomerId())
				.build();
	}

	public static PickupDetails toPickupDetails(OrderFulfillment fulfillment) {
		return new PickupDetails.Builder()
				.state(PickupState.valueOf(fulfillment.getState()))
//				.pickupTimeRequested(LocalDateTime.parse(fulfillment.getPickupDetails().getPickedUpAt(), DateTimeFormatter.ISO_INSTANT))
				.fulfillmentId(fulfillment.getUid())
				.build();
	}

	public static Costs toCosts(OrderMoneyAmounts amounts) {
		return new Costs.Builder()
				.taxAmount((double) amounts.getTaxMoney().getAmount() / CURRENCY_DENOMINATOR_CONVERTER)
				.totalAmount((double) amounts.getTotalMoney().getAmount() / CURRENCY_DENOMINATOR_CONVERTER)
				.build();
	}
}
