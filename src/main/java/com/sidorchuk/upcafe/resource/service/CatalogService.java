package com.sidorchuk.upcafe.resource.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sidorchuk.upcafe.resource.entity.catalog.Category;
import com.sidorchuk.upcafe.resource.entity.catalog.Image;
import com.sidorchuk.upcafe.resource.entity.catalog.Item;
import com.sidorchuk.upcafe.resource.entity.catalog.ItemModifierList;
import com.sidorchuk.upcafe.resource.entity.catalog.ItemVariation;
import com.sidorchuk.upcafe.resource.entity.catalog.Modifier;
import com.sidorchuk.upcafe.resource.entity.catalog.ModifierList;
import com.sidorchuk.upcafe.resource.model.catalog.Catalog;
import com.sidorchuk.upcafe.resource.model.catalog.CategoryItem;
import com.sidorchuk.upcafe.resource.model.catalog.ItemData;
import com.sidorchuk.upcafe.resource.model.catalog.LineItem;
import com.sidorchuk.upcafe.resource.model.catalog.ModifierData;
import com.sidorchuk.upcafe.resource.model.catalog.ModifierListData;
import com.sidorchuk.upcafe.resource.model.catalog.VariationData;
import com.sidorchuk.upcafe.resource.repository.catalog.CategoryRepository;
import com.sidorchuk.upcafe.resource.repository.catalog.ImageRepository;
import com.sidorchuk.upcafe.resource.repository.catalog.ItemModifierListRepository;
import com.sidorchuk.upcafe.resource.repository.catalog.ItemRepository;
import com.sidorchuk.upcafe.resource.repository.catalog.ModifierListRepository;
import com.sidorchuk.upcafe.resource.repository.catalog.ModifierRepository;
import com.sidorchuk.upcafe.resource.repository.catalog.VariationRepository;
import com.squareup.square.Environment;
import com.squareup.square.SquareClient;
import com.squareup.square.api.CatalogApi;
import com.squareup.square.exceptions.ApiException;
import com.squareup.square.models.CatalogImage;
import com.squareup.square.models.CatalogItemModifierListInfo;
import com.squareup.square.models.CatalogModifierList;
import com.squareup.square.models.CatalogObject;
import com.squareup.square.models.CreateCatalogImageRequest;
import com.squareup.square.models.ListCatalogResponse;

@Service
public class CatalogService {

	private final String IMAGE 						= "IMAGE";
	private final String MODIFIER_LIST 				= "MODIFIER_LIST";
	private final String ITEM 						= "ITEM";
	private final String CATEGORY					= "CATEGORY";
	
	private final double LOWEST_CURRENCY_DENOMINATOR 	= 100;

	private SquareClient client = new SquareClient.Builder().environment(Environment.PRODUCTION)
			.accessToken(System.getenv("SQUARE_PROD")).build();

	private CatalogApi catalogApi = client.getCatalogApi();

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
	
	public void updateLocalCatalog() {
		
		String batchUpdateId = UUID.randomUUID().toString();
		
		System.out.println("CURRENT BATCH ID : " + batchUpdateId);

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
					saveCategory(catalogObject);
				});
			}
//
//			ListCatalogResponse response = retrieveFromSquare(IMAGE);
//
//			if (response.getObjects() != null) {
//				response.getObjects().forEach(catalogObject -> {
//					saveImage(catalogObject, batchUpdateId);
//					removeImagesNotInSquare(batchUpdateId);
//				});
//			}
			

		} catch (ApiException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			client.shutdown();
		}
	}

	private ListCatalogResponse retrieveFromSquare(String type) throws ApiException, IOException {
		return catalogApi.listCatalog(null, type);
	}
	

	private void saveCategory(CatalogObject catalogObject) {
		Category category = new Category();
		
		category.setId(catalogObject.getId());
		category.setName(catalogObject.getCategoryData().getName());
		
		System.out.println(category.toString());
		
		categoryRepository.save(category);
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
			
			System.out.println(item.toString());
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

	private void createImage(String catalogObjectId, String imageUrl, String caption, String nameOfPicture) {

		CatalogImage requestImageImageData = new CatalogImage.Builder().caption(caption).name(nameOfPicture).build();

		CatalogObject requestImage = new CatalogObject.Builder("IMAGE", "#TEMP_ID").imageData(requestImageImageData)
				.build();
		CreateCatalogImageRequest request = new CreateCatalogImageRequest.Builder(UUID.randomUUID().toString())
				.objectId(catalogObjectId).image(requestImage).build();

		try {
			catalogApi.createCatalogImage(request, new File(imageUrl));
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Category> getCategories() {
		return categoryRepository.findAll();
	}
	
	public Catalog getCatalogByCategory(String category) {
		Catalog catalog = new Catalog();
		catalog.setCategory(category);
		
		List<Item> items = itemRepository.getItemsByCategoryName(category);
		items.forEach(item -> {
			
			CategoryItem lineItem = new CategoryItem();

			lineItem.setItemData(getItemData(item));
			lineItem.setVariationsData(getVariationsData(item.getItemId()));
			lineItem.setModifierListsData(getModifierListsData(item.getItemId()));
			
			catalog.getItems().add(lineItem);
		});
		
		return catalog;
	}
	
	private List<ModifierListData> getModifierListsData(String itemId) {
		
		
		List<ModifierListData> modifierListsData = new ArrayList<ModifierListData>();
		
		itemModListRepository.getItemModifierListsByItemId(itemId).forEach(itemModListId -> {
			
			Optional<ModifierList> modList = modListRepository.findById(itemModListId.getModifierListId());
			
			modList.ifPresent(dbList -> {
				ModifierListData listData = new ModifierListData();
				listData.setNameOfList(dbList.getName());
				
				if(dbList.getImage() != null)
					listData.setImageUrl(dbList.getImage().getUrl());
				
				listData.setSelectionType(dbList.getSelectionType());
			
				listData.setModifiers(getModifiersData(dbList.getId()));
				modifierListsData.add(listData);
			});
		});
		
		return modifierListsData;
	}
	
	private List<ModifierData> getModifiersData(String modListId) {
		
		List<ModifierData> modifiersData = new ArrayList<ModifierData>();
		
		modifierRepository.getModifiersByModListId(modListId).forEach(modifier -> {
			ModifierData data = new ModifierData();
			data.setId(modifier.getId());
			data.setName(modifier.getName());
			data.setOnByDefault(modifier.isOnByDefault());
			data.setPrice(modifier.getPrice());
			
			modifiersData.add(data);
		});
		
		return modifiersData;
	}
	
	private List<VariationData> getVariationsData(String itemId) {
		List<VariationData> variations = new ArrayList<VariationData>();
		
		variationRepository.getVariationsByItemItemId(itemId).forEach(variation -> {
			VariationData varData = new VariationData();
			varData.setName(variation.getName());
			varData.setVariationId(variation.getVariationId());
			
			if(variation.getImage() != null)
				varData.setVariationImageUrl(variation.getImage().getUrl());
			
			varData.setVariationPrice(variation.getPrice());
			
			variations.add(varData);
		});
		
		return variations;
	}
	
	private VariationData getSingleVariationData(ItemVariation variation)  {
		VariationData variationData = new VariationData();
		
		variationData.setName(variation.getName());
		variationData.setVariationId(variation.getVariationId());
		
		if(variation.getImage() != null)
			variationData.setVariationImageUrl(variation.getImage().getUrl());
		
		variationData.setVariationPrice(variation.getPrice());
		
		return variationData;
	}
	
	private ItemData getItemData(Item item) {
		ItemData itemData = new ItemData();
		itemData.setName(item.getName());
		itemData.setDescription(item.getDescription());
		itemData.setImage(item.getImage());
		return itemData;
	}
	
	public LineItem getCategoryItemByVariationId(String id) {
		LineItem lineItem = new LineItem();
		
		Optional<ItemVariation> variation = variationRepository.findById(id);
		
		lineItem.setVariationData(getSingleVariationData(variation.get()));
		lineItem.setItemData(getItemData(variation.get().getItem()));
		lineItem.setModifierListsData(getModifierListsData(variation.get().getItem().getItemId()));
		
		return lineItem;
	}

}
