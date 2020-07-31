package upcafe.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.squareup.square.utilities.FileWrapper;
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
import upcafe.dto.catalog.ModifierListDTO;
import upcafe.dto.catalog.VariationDTO;
import upcafe.entity.catalog.Image;
import upcafe.entity.catalog.Item;
import upcafe.error.NonExistentIdFoundException;
import upcafe.repository.catalog.CategoryRepository;
import upcafe.repository.catalog.ImageRepository;
import upcafe.repository.catalog.ItemRepository;
import upcafe.repository.catalog.ModifierListRepository;
import upcafe.repository.catalog.ModifierRepository;
import upcafe.repository.catalog.VariationRepository;
import upcafe.utils.TransferUtils;


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
	    	
	    	return catalogApi.createCatalogImage(request, new FileWrapper(file)).getImage();
		} catch (IllegalStateException | IOException | ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return null;
    }
    
    public Image saveImageLocally(CatalogObject squareImage) {
    	if(squareImage == null) throw new NonExistentIdFoundException("NULL", "CatalogObject");
    	
    	return imageRepo.save(new Image.Builder(squareImage.getId())
    			.caption(squareImage.getImageData().getCaption())
    			.name(squareImage.getImageData().getName())
    			.url(squareImage.getImageData().getUrl())
    			.build());
    }
    
    public boolean assignImageToObjectLocally(CatalogObject catalogObject) {
    	
    	final String TYPE = catalogObject.getType();
    	
    	if(TYPE.compareTo("MODIFIER") == 0) {
    		modifierRepository.assignImageToModifier(catalogObject.getImageId(), catalogObject.getId());
    		return true;
    	} else if(TYPE.compareTo("MODIFIER_LIST") == 0) {
    		modifierListRepository.assignImageToModifierList(catalogObject.getImageId(), catalogObject.getId());
    		return true;
    	} else if(TYPE.compareTo("ITEM_VARIATION") == 0) {
    		variationRepository.assignImageToVariation(catalogObject.getImageId(), catalogObject.getId());
    		return true;
    	} else {
    		throw new UnsupportedOperationException(TYPE + " can not have an image assigned");
    	}
    }
    
    public CatalogObject getSquareCatalogObjectById(String objectId) {
    	
    	if(objectId == null) throw new NonExistentIdFoundException("NULL", "CatalogObject");
    	
    	if(objectId.length() <= 1) throw new NonExistentIdFoundException(objectId, "CatalogObject");
    	
    	CatalogApi catalogApi = client.getCatalogApi();
    	
    	try {
			return catalogApi.retrieveCatalogObject(objectId, false).getObject();
		} catch (ApiException | IOException e) {
			throw new NonExistentIdFoundException(objectId, "CatalogObject");
		}
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
                        .image(TransferUtils.toImageDTO(variationDB.getImage()))
                        .price(variationDB.getPrice())
                        .build());
            });

            // Since modifier lists can be assigned to more than one item, add it to the set of modifier lists
            // only if it's not present yet.
            TransferUtils.toModifierListDTOs(itemDB.getModifierLists())
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
