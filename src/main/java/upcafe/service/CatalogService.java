package upcafe.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.squareup.square.SquareClient;
import com.squareup.square.api.CatalogApi;
import com.squareup.square.exceptions.ApiException;
import com.squareup.square.models.CatalogImage;
import com.squareup.square.models.CatalogObject;
import com.squareup.square.models.CreateCatalogImageRequest;

import upcafe.dto.catalog.CatalogDTO;
import upcafe.dto.catalog.CatalogInventoryUpdate;
import upcafe.dto.catalog.CategoryDTO;
import upcafe.dto.catalog.ImageDTO;
import upcafe.dto.catalog.ModifierDTO;
import upcafe.dto.catalog.ModifierListDTO;
import upcafe.dto.catalog.VariationDTO;
import upcafe.dto.menu.MenuItemDTO;
import upcafe.entity.catalog.Image;
import upcafe.entity.catalog.Item;
import upcafe.entity.catalog.Modifier;
import upcafe.entity.catalog.ModifierList;
import upcafe.repository.catalog.CategoryRepository;
import upcafe.repository.catalog.ImageRepository;
import upcafe.repository.catalog.ItemRepository;
import upcafe.repository.catalog.ModifierListRepository;
import upcafe.repository.catalog.ModifierRepository;
import upcafe.repository.catalog.VariationRepository;


@Service
public class CatalogService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private VariationRepository variationRepository;

    @Autowired
    private ModifierRepository modifierRepository;

    @Autowired
    private ModifierListRepository modifierListRepository;
    
    @Autowired
    private ImageRepository imageRepo;

    @Autowired
    private SquareClient client;

    public CatalogObject createImage(MultipartFile imageSaveRequest, String objectId) {
    	
    	File file = new File(System.getProperty("user.dir") + "\\file-to-save.png");
    	System.out.println("\n\n\n\n\n\n\n- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  -" 
    			+ file.getPath() + "- - - - - - - - - - - -- - - - - - - - - - - - - - - - - - - - - - - -\n\n\n\n\n\n\n");
    	
    	try {
    		
			imageSaveRequest.transferTo(file);
	    	CatalogApi catalogApi = client.getCatalogApi();
	    	
	    	CatalogImage imageData = new CatalogImage.Builder()
	    			.build();
	    	
	    	CatalogObject image = new CatalogObject.Builder("IMAGE", "#TEMP_ID")
	    			.imageData(imageData)
	    			.build();
	    	
	    	CreateCatalogImageRequest request = new CreateCatalogImageRequest.Builder(UUID.randomUUID().toString())
	    			.objectId(objectId)
	    			.image(image)
	    			.build();
	    	
	    	return catalogApi.createCatalogImage(request, file).getImage();
		} catch (IllegalStateException | IOException | ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return null;
    }
    
    public Image saveImageLocally(CatalogObject squareImage) {
    	return imageRepo.save(new Image.Builder(squareImage.getId())
    			.caption(squareImage.getImageData().getCaption())
    			.name(squareImage.getImageData().getName())
    			.url(squareImage.getImageData().getUrl())
    			.build());
    }
    
    public void assignImageToObjectLocally(CatalogObject catalogObject) {
    	
    	final String TYPE = catalogObject.getType();
    	
    	if(TYPE.compareTo("MODIFIER") == 0) {
    		modifierRepository.assignImageToModifier(catalogObject.getImageId(), catalogObject.getId());
    	} else if(TYPE.compareTo("MODIFIER_LIST") == 0) {
    		modifierListRepository.assignImageToModifierList(catalogObject.getImageId(), catalogObject.getId());
    	} else if(TYPE.compareTo("ITEM_VARIATION") == 0) {
    		variationRepository.assignImageToVariation(catalogObject.getImageId(), catalogObject.getId());
    	} else {
    		System.out.println(TYPE + " can not have an image assigned");
    	}
    }
    
    public CatalogObject getSquareCatalogObjectById(String objectId) {
    	CatalogApi catalogApi = client.getCatalogApi();
    	
    	try {
			return catalogApi.retrieveCatalogObject(objectId, false).getObject();
		} catch (ApiException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return null;
    }
    
    // TODO: Test getCatalog()
    public CatalogDTO getCatalog() {
    	
        List<Item> items = itemRepository.findAll();

        List<VariationDTO> variationDTOsList = new ArrayList<VariationDTO>();
        Set<ModifierListDTO> modifierListDTOList = new HashSet<ModifierListDTO>();

        items.forEach(itemDB -> {

            itemDB.getVariations().forEach(variationDB -> {
                String nameOfMenuItem = (variationDB.getName().compareTo("Regular") == 0)
                        ? itemDB.getName()
                        : variationDB.getName();

                variationDTOsList.add(new VariationDTO.Builder(variationDB.getId())
                        .name(nameOfMenuItem)
                        .inStock(variationDB.getInStock())
                        .image(transferToImageDTO(variationDB.getImage()))
                        .price(variationDB.getPrice())
                        .build());
            });

            transferToListOfModifierListDTOs(itemDB.getModifierLists())
                    .forEach(modifierListToStore -> {
                        if (!modifierListDTOList.stream().anyMatch(modListAlreadyStored
                                -> modListAlreadyStored.getId().compareTo(modifierListToStore.getId()) == 0))
                            modifierListDTOList.add(modifierListToStore);
                    });
        });

        return new CatalogDTO.Builder()
                .modifierLists(modifierListDTOList)
                .itemsList(variationDTOsList)
                .build();
    }

    
    // TODO: test getItemsForCategory
    public List<MenuItemDTO> getItemsForCategory(String category) {

        List<Item> items = itemRepository.getItemsByCategoryName(category);
        List<MenuItemDTO> itemsDTO = new ArrayList<MenuItemDTO>();

        items.forEach(itemDB -> {

            itemDB.getVariations().forEach(variationDB -> {

                String nameOfMenuItem = (variationDB.getName().compareTo("Regular") == 0)
                        ? itemDB.getName()
                        : variationDB.getName();

                MenuItemDTO menuItemDTO = new MenuItemDTO.Builder(variationDB.getId())
                        .description(itemDB.getDescription())
                        .image(transferToImageDTO(variationDB.getImage()))
                        .inStock(variationDB.getInStock())
                        .modifierLists(transferToListOfModifierListDTOs(itemDB.getModifierLists()))
                        .name(nameOfMenuItem)
                        .price(variationDB.getPrice())
                        .build();

                itemsDTO.add(menuItemDTO);
            });
        });

        return itemsDTO;
    }

    
    // TODO: Test transferToListOfModifierListDTOs
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

    
    // TODO: Test transferToImageDTO
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

    
    // TODO: Test transferToListOfModifierDTOs
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

    
    // TODO: Test getCategories()
    public List<CategoryDTO> getCategories() {
        List<CategoryDTO> categories = new ArrayList<CategoryDTO>();
        categoryRepository.findAll().forEach(category -> {
            categories.add(new CategoryDTO.Builder().name(category.getName()).id(category.getId()).build());
        });

        return categories;
    }

    
    // TODO: Test updateInventory
    public boolean updateInventory(CatalogInventoryUpdate inventory) {
        inventory.getVariations().forEach(variation -> {
            variationRepository.updateInventoryById(variation.getId(), variation.getInStock());
        });

        inventory.getModifiers().forEach(modifier -> {
            modifierRepository.updateInventoryById(modifier.getId(), modifier.getInStock());
        });

        return true;
    }
}
