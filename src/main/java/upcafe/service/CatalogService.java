package upcafe.service;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.squareup.square.SquareClient;
import com.squareup.square.exceptions.ApiException;
import com.squareup.square.models.CatalogImage;
import com.squareup.square.models.CatalogObject;
import com.squareup.square.models.CreateCatalogImageRequest;
import com.squareup.square.models.ListCatalogResponse;

import upcafe.dto.catalog.ModifierListDTO;
import upcafe.dto.menu.MenuItemDTO;
import upcafe.dto.catalog.ModifierDTO;
import upcafe.entity.catalog.Image;
import upcafe.entity.catalog.Item;
import upcafe.entity.catalog.Modifier;
import upcafe.entity.catalog.ModifierList;
import upcafe.repository.catalog.*;

import upcafe.dto.catalog.*;


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
    private ImageRepository imageRepo;

    @Autowired
    private SquareClient client;

    
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
