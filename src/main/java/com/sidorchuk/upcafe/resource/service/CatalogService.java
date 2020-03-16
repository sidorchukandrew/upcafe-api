package com.sidorchuk.upcafe.resource.service;

import java.io.File;
import java.io.IOException;
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
import com.squareup.square.models.CatalogItemVariation;
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
			.accessToken("").build();

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

		try {

			ListCatalogResponse response = retrieveFromSquare(IMAGE);

			if (response.getObjects() != null) {
				response.getObjects().forEach(catalogObject -> {
					saveImage(catalogObject);
				});
			}
			
			response = retrieveFromSquare(CATEGORY);
			
			if(response.getObjects() != null) {
				response.getObjects().forEach(catalogObject -> {
					saveCategory(catalogObject);
				});
			}

			response = retrieveFromSquare(MODIFIER_LIST);
			
			if (response.getObjects() != null) {
				response.getObjects().forEach(catalogObject -> {
					saveModifierList(catalogObject);
				});
			}
			
			response = retrieveFromSquare(ITEM);
			
			if (response.getObjects() != null) {
				response.getObjects().forEach(catalogObject -> {
					saveItem(catalogObject);
				});
			}

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
	
	private void saveModifierList(CatalogObject modifierListObject) {

		CatalogModifierList modifierList = modifierListObject.getModifierListData();

		ModifierList modifiers = new ModifierList();
		modifiers.setId(modifierListObject.getId());
		modifiers.setName(modifierList.getName());
		modifiers.setSelectionType(modifierList.getSelectionType());
		
		if(modifierListObject.getImageId() != null)
			modifiers.setImage(new Image(modifierListObject.getImageId(), null, null, null));
		
		System.out.println("\n" + modifiers.toString());

		modListRepository.save(modifiers);

		modifierListObject.getModifierListData().getModifiers().forEach(m -> {
			saveModifier(m, modifiers);
		});
	}

	private void saveModifier(CatalogObject m, ModifierList modList) {
		Modifier modifier = new Modifier();
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

	private void saveImage(CatalogObject object) {
		Image image = new Image();
		image.setImageId(object.getId());
		image.setName(object.getImageData().getName());
		image.setUrl(object.getImageData().getUrl());
		image.setCaption(object.getImageData().getCaption());

		System.out.println(image.toString());
		imageRepository.save(image);
	}

	private void saveItem(CatalogObject object) {

		Item item = new Item();
		
		item.setItemId(object.getId());
		item.setDescription(object.getItemData().getDescription());
		item.setName(object.getItemData().getName());
		item.setCategory(new Category(object.getItemData().getCategoryId(), null));
		
		if(object.getImageId() != null)
			item.setImage(new Image(object.getImageId(), null, null, null));
		
		itemRepository.save(item);

		System.out.println("\n" + item.toString());

		object.getItemData().getVariations().forEach(variationObject -> {
			saveItemVariation(variationObject);
		});

		if(object.getItemData().getModifierListInfo() != null) {
			object.getItemData().getModifierListInfo().forEach(modifierListInfo -> {
				ItemModifierList itemModifierList = new ItemModifierList();

				itemModifierList.setItemId(object.getId());
				itemModifierList.setModifierList(modifierListInfo.getModifierListId());

				System.out.println("\t" + itemModifierList.toString());
				itemModListRepository.save(itemModifierList);
			});
		}

	}

	private void saveItemVariation(CatalogObject variationObject) {
		ItemVariation variation = new ItemVariation();
		Item item = new Item();
		item.setItemId(variationObject.getItemVariationData().getItemId());
		
		variation.setName(variationObject.getItemVariationData().getName());
		variation.setPrice(variationObject.getItemVariationData().getPriceMoney().getAmount() / LOWEST_CURRENCY_DENOMINATOR);
		variation.setItem(item);
		variation.setVariationId(variationObject.getId());
		
		if(variationObject.getImageId() != null)
			variation.setImage(new Image(variationObject.getImageId(), null, null, null));

		System.out.println(variation.toString());
		
		variationRepository.save(variation);
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
}
