package upcafe.service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.squareup.square.Environment;
import com.squareup.square.SquareClient;
import com.squareup.square.api.CatalogApi;
import com.squareup.square.exceptions.ApiException;
import com.squareup.square.models.CatalogItemModifierListInfo;
import com.squareup.square.models.CatalogModifierList;
import com.squareup.square.models.CatalogObject;
import com.squareup.square.models.ListCatalogResponse;

import upcafe.entity.catalog.Category;
import upcafe.entity.catalog.Image;
import upcafe.entity.catalog.Item;
import upcafe.entity.catalog.ItemModifierList;
import upcafe.entity.catalog.ItemVariation;
import upcafe.entity.catalog.Modifier;
import upcafe.entity.catalog.ModifierList;
import upcafe.repository.catalog.CategoryRepository;
import upcafe.repository.catalog.ImageRepository;
import upcafe.repository.catalog.ItemModifierListRepository;
import upcafe.repository.catalog.ItemRepository;
import upcafe.repository.catalog.ModifierListRepository;
import upcafe.repository.catalog.ModifierRepository;
import upcafe.repository.catalog.VariationRepository;

@Configuration
@EnableScheduling
public class UpdateService {

	private final String IMAGE 						= "IMAGE";
	private final String MODIFIER_LIST 				= "MODIFIER_LIST";
	private final String ITEM 						= "ITEM";
	private final String CATEGORY					= "CATEGORY";
	public static final String ANSI_RESET 			= "\u001B[0m";
	public static final String ANSI_GREEN 			= "\u001B[32m";
	public static final String ANSI_BLUE 			= "\u001B[34m";
	
	private final double LOWEST_CURRENCY_DENOMINATOR 	= 100;

	@Bean
	public SquareClient client() {
		SquareClient client = new SquareClient.Builder().environment(Environment.SANDBOX)
				.accessToken(System.getenv("SQUARE_SANDBOX")).build();

		return client;
	}

	@Autowired
	private ModifierListRepository modListRepository;
	@Autowired
	private ModifierRepository modifierRepository;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private VariationRepository variationRepository;
	@Autowired
	private ItemModifierListRepository itemModListRepository;
	
	@Scheduled(cron = "0 0 0 * * *")
	public void updateLocalCatalog() {
		
		String batchUpdateId = UUID.randomUUID().toString();
		
		System.out.println(ANSI_BLUE + "CURRENT BATCH ID : " + batchUpdateId + ANSI_RESET);

		try {
			
			ListCatalogResponse response = retrieveFromSquare(MODIFIER_LIST);
			
			if (response.getObjects() != null) {
				response.getObjects().forEach(catalogObject -> {
					saveModifierList(catalogObject, batchUpdateId);
				});
			}
			
			modifierRepository.deleteOldBatchUpdateIds(batchUpdateId);
			modListRepository.deleteOldBatchUpdateIds(batchUpdateId);

			response = retrieveFromSquare(ITEM);
			
			if (response.getObjects() != null) {
				response.getObjects().forEach(catalogObject -> {
					saveItem(catalogObject, batchUpdateId);
				});
			}
			
			itemModListRepository.deleteOldBatchUpdateIds(batchUpdateId);
			variationRepository.deleteOldBatchUpdateIds(batchUpdateId);
			itemRepository.deleteOldBatchUpdateIds(batchUpdateId);
			
			response = retrieveFromSquare(CATEGORY);
			
			if(response.getObjects() != null) {
				response.getObjects().forEach(catalogObject -> {
					saveCategory(catalogObject, batchUpdateId);
				});
			}
			
			categoryRepository.deleteOldBatchUpdateIds(batchUpdateId);

			response = retrieveFromSquare(IMAGE);

			if (response.getObjects() != null) {
				response.getObjects().forEach(catalogObject -> {
					saveImage(catalogObject, batchUpdateId);
				});
			}
			
			imageRepository.deleteOldBatchUpdateIds(batchUpdateId);
			System.out.println(ANSI_GREEN + "Update completed successfully." + ANSI_RESET);

		} catch (ApiException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			client().shutdown();
		}
	}

	private ListCatalogResponse retrieveFromSquare(String type) throws ApiException, IOException {
		return client().getCatalogApi().listCatalog(null, type);
	}
	

	private void saveCategory(CatalogObject catalogObject, String batchUpdateId) {
		
		Optional<Category> optCategory = categoryRepository.findById(catalogObject.getId());
		
		if(!optCategory.isPresent()) {
			
			Category category = new Category();
			
			category.setId(catalogObject.getId());
			category.setName(catalogObject.getCategoryData().getName());
			category.setBatchUpdateId(batchUpdateId);
			category.setUpdatedAt(catalogObject.getUpdatedAt());
			
			System.out.println(category.toString());
			
			categoryRepository.save(category);
		}
		else {
			if(optCategory.get().getUpdatedAt() == null ) {
				optCategory.get().setBatchUpdateId(batchUpdateId);
				optCategory.get().setName(catalogObject.getCategoryData().getName());
				optCategory.get().setUpdatedAt(catalogObject.getUpdatedAt());
			}
			else if(optCategory.get().getUpdatedAt().compareTo(catalogObject.getUpdatedAt()) != 0) {
				optCategory.get().setBatchUpdateId(batchUpdateId);
				optCategory.get().setName(catalogObject.getCategoryData().getName());
				optCategory.get().setUpdatedAt(catalogObject.getUpdatedAt());
			}
			else {
				optCategory.get().setBatchUpdateId(batchUpdateId);
			}
			
			categoryRepository.save(optCategory.get());
		}
	}
	
	private void saveModifierList(CatalogObject modifierListObject, String batchUpdateId) {

		Optional<ModifierList> optModList = modListRepository.findById(modifierListObject.getId());
		
		if(!optModList.isPresent()) {
			
			CatalogModifierList modifierList = modifierListObject.getModifierListData();
			
			ModifierList modifiers = new ModifierList();
			modifiers.setId(modifierListObject.getId());
			modifiers.setName(modifierList.getName());
			modifiers.setSelectionType(modifierList.getSelectionType());
			modifiers.setBatchUpdateId(batchUpdateId);
			modifiers.setUpdatedAt(modifierListObject.getUpdatedAt());
			
			if(modifierListObject.getImageId() != null)
				modifiers.setImage(new Image(modifierListObject.getImageId(), null, null, null, null, null));
			
			System.out.println("\n" + modifiers.toString());
			
			modListRepository.save(modifiers);
			
			modifierListObject.getModifierListData().getModifiers().forEach(m -> {
				saveModifier(m, modifiers, batchUpdateId);
			});
		}
		
		else {
			if(optModList.get().getUpdatedAt().compareTo(modifierListObject.getUpdatedAt()) != 0) {
				optModList.get().setBatchUpdateId(batchUpdateId);
				optModList.get().setUpdatedAt(modifierListObject.getUpdatedAt());
				optModList.get().setName(modifierListObject.getModifierListData().getName());
				optModList.get().setSelectionType(modifierListObject.getModifierListData().getSelectionType());
				
				if(modifierListObject.getImageId() != null)
					optModList.get().setImage(new Image(modifierListObject.getImageId(), null, null, null, null, null));
			}
			else {
				optModList.get().setBatchUpdateId(batchUpdateId);
			}
			
			modListRepository.save(optModList.get());
			
			modifierListObject.getModifierListData().getModifiers().forEach(m -> {
				saveModifier(m, optModList.get(), batchUpdateId);
			});
		}
	}

	private void saveModifier(CatalogObject m, ModifierList modList, String batchUpdateId) {
		
		Optional<Modifier> optModifier = modifierRepository.findById(m.getId());
		
		if(!optModifier.isPresent()) {
			
			Modifier modifier = new Modifier();
			modifier.setBatchUpdateId(batchUpdateId);
			modifier.setUpdatedAt(m.getUpdatedAt());
			modifier.setId(m.getId());
			modifier.setModifierList(modList);

			if(m.getModifierData().getPriceMoney() != null) {
				if (m.getModifierData().getPriceMoney().getCurrency().compareTo("USD") == 0) {
					modifier.setPrice(m.getModifierData().getPriceMoney().getAmount() / LOWEST_CURRENCY_DENOMINATOR);
				}
			}
			
			modifier.setOnByDefault(false);
			modifier.setName(m.getModifierData().getName());
			
			modifierRepository.save(modifier);
			System.out.println("\t" + modifier.toString());
		}
		else {
			if(optModifier.get().getUpdatedAt().compareTo(m.getUpdatedAt()) != 0) {
				optModifier.get().setUpdatedAt(m.getUpdatedAt());
				optModifier.get().setBatchUpdateId(batchUpdateId);

				optModifier.get().setModifierList(modList);
				optModifier.get().setName(m.getModifierData().getName());
				
				if(m.getModifierData().getPriceMoney() != null) {
					if (m.getModifierData().getPriceMoney().getCurrency().compareTo("USD") == 0) {
						optModifier.get().setPrice(m.getModifierData().getPriceMoney().getAmount() / LOWEST_CURRENCY_DENOMINATOR);
					}
				}
			}
			
			else {
				optModifier.get().setBatchUpdateId(batchUpdateId);
			}
			
			modifierRepository.save(optModifier.get());
		}
	}

	private void saveImage(CatalogObject object, String batchUpdateId) {
		
		Optional<Image> optImage = imageRepository.findById(object.getId());
		
		if(!optImage.isPresent()) {			
			Image image = new Image();
			image.setImageId(object.getId());
			image.setName(object.getImageData().getName());
			image.setUrl(object.getImageData().getUrl());
			image.setCaption(object.getImageData().getCaption());
			image.setUpdatedAt(object.getUpdatedAt());
			image.setBatchUpdateId(batchUpdateId);
			
			System.out.println(image.toString());
			imageRepository.save(image);
		}
		else {
			if(optImage.get().getUpdatedAt() == null) {
				optImage.get().setBatchUpdateId(batchUpdateId);
				optImage.get().setCaption(object.getImageData().getCaption());
				optImage.get().setName(object.getImageData().getName());
				optImage.get().setUrl(object.getImageData().getUrl());
				optImage.get().setUpdatedAt(object.getUpdatedAt());
			}
			else if (optImage.get().getUpdatedAt().compareTo(object.getUpdatedAt()) != 0) {
				optImage.get().setBatchUpdateId(batchUpdateId);
				optImage.get().setCaption(object.getImageData().getCaption());
				optImage.get().setName(object.getImageData().getName());
				optImage.get().setUrl(object.getImageData().getUrl());
				optImage.get().setUpdatedAt(object.getUpdatedAt());
			}
			else {
				optImage.get().setBatchUpdateId(batchUpdateId);
			}
			
			imageRepository.save(optImage.get());
		}
	}

	private void saveItem(CatalogObject object, String batchUpdateId) {
		
		Optional<Item> optItem = itemRepository.findById(object.getId());
		
		// Create a new item and save it
		if(!optItem.isPresent()) {
			Item item = new Item();
			
			item.setItemId(object.getId());
			item.setDescription(object.getItemData().getDescription());
			item.setName(object.getItemData().getName());
			item.setCategory(new Category(object.getItemData().getCategoryId(), null, null, null));
			item.setBatchUpdateId(batchUpdateId);
			item.setUpdatedAt(object.getUpdatedAt());
			
			if(object.getImageId() != null)
				item.setImage(new Image(object.getImageId(), null, null, null, null, null));
			
			itemRepository.save(item);
			
			object.getItemData().getVariations().forEach(variationObject -> {
				saveItemVariation(variationObject, batchUpdateId);
			});
			
			if(object.getItemData().getModifierListInfo() != null) {
				object.getItemData().getModifierListInfo().forEach(modifierListInfo -> {
					saveItemModList(object, modifierListInfo, batchUpdateId);
				});
			}
		}
		
		// The item already exists in the DB, it's not a new item
		else {
			
			//If the item has been updated in Square, update it locally
			if(optItem.get().getUpdatedAt().compareTo(object.getUpdatedAt()) != 0) {
				optItem.get().setItemId(object.getId());
				optItem.get().setDescription(object.getItemData().getDescription());
				optItem.get().setName(object.getItemData().getName());
				optItem.get().setCategory(new Category(object.getItemData().getCategoryId(), null, null, null));
				optItem.get().setBatchUpdateId(batchUpdateId);
				optItem.get().setUpdatedAt(object.getUpdatedAt());
				
				if(object.getImageId() != null)
					optItem.get().setImage(new Image(object.getImageId(), null, null, null, null, null));
			
				itemRepository.save(optItem.get());
				System.out.println(optItem.get().toString());
			}

			//Just update the batch id
			else {
				optItem.get().setBatchUpdateId(batchUpdateId);
				itemRepository.save(optItem.get());
			}
			
			object.getItemData().getVariations().forEach(variationObject -> {
				saveItemVariation(variationObject, batchUpdateId);
			});
			

			if(object.getItemData().getModifierListInfo() != null) {
				object.getItemData().getModifierListInfo().forEach(modifierListInfo -> {
					saveItemModList(object, modifierListInfo, batchUpdateId);
				});
			}
		}
	}
	
	private void saveItemModList(CatalogObject itemObject, CatalogItemModifierListInfo modList, String batchUpdateId) {
		
		ItemModifierList itemModifierList = new ItemModifierList();

		itemModifierList.setItemId(itemObject.getId());
		itemModifierList.setModifierList(modList.getModifierListId());
		itemModifierList.setBatchUpdateId(batchUpdateId);
		
		itemModListRepository.save(itemModifierList);			

	}

	private void saveItemVariation(CatalogObject variationObject, String batchUpdateId) {
		
		Optional<ItemVariation> optVariation = variationRepository.findById(variationObject.getId());
		
		if(!optVariation.isPresent()) {
			
			ItemVariation variation = new ItemVariation();
			Item item = new Item();
			
			item.setItemId(variationObject.getItemVariationData().getItemId());
			variation.setName(variationObject.getItemVariationData().getName());
			variation.setPrice(variationObject.getItemVariationData().getPriceMoney().getAmount() / LOWEST_CURRENCY_DENOMINATOR);
			variation.setItem(item);
			variation.setVariationId(variationObject.getId());
			variation.setBatchUpdateId(batchUpdateId);
			variation.setUpdatedAt(variationObject.getUpdatedAt());
			if(variationObject.getImageId() != null)
				variation.setImage(new Image(variationObject.getImageId(), null, null, null, null, null));

			System.out.println(variation.toString());

			variationRepository.save(variation);
		}
		//Item exists, so we need to check if its been updated
		else {
		
			if(optVariation.get().getUpdatedAt().compareTo(variationObject.getUpdatedAt()) != 0) {

				Item item = new Item();
				
				item.setItemId(variationObject.getItemVariationData().getItemId());
				optVariation.get().setName(variationObject.getItemVariationData().getName());
				optVariation.get().setPrice(variationObject.getItemVariationData().getPriceMoney().getAmount() / LOWEST_CURRENCY_DENOMINATOR);
				optVariation.get().setItem(item);
				optVariation.get().setVariationId(variationObject.getId());
				optVariation.get().setBatchUpdateId(batchUpdateId);
				optVariation.get().setUpdatedAt(variationObject.getUpdatedAt());
				if(variationObject.getImageId() != null)
					optVariation.get().setImage(new Image(variationObject.getImageId(), null, null, null, null, null));
			}
			else {
				optVariation.get().setBatchUpdateId(batchUpdateId);
			}
			variationRepository.save(optVariation.get());
		}
	}

}
