package upcafe.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.squareup.square.SquareClient;
import com.squareup.square.exceptions.ApiException;
import com.squareup.square.models.CatalogImage;
import com.squareup.square.models.CatalogObject;
import com.squareup.square.models.CreateCatalogImageRequest;
import com.squareup.square.models.ListCatalogResponse;

import upcafe.dto.catalog.ModifierListDTO;
import upcafe.dto.catalog.ModifierDTO;
import upcafe.entity.catalog.Category;
import upcafe.entity.catalog.Image;
import upcafe.entity.catalog.Item;
import upcafe.entity.catalog.Modifier;
import upcafe.entity.catalog.Variation;
import upcafe.entity.catalog.ModifierList;
import upcafe.model.catalog.Catalog;
import upcafe.model.catalog.CategoryItem;
import upcafe.model.catalog.ItemData;
import upcafe.model.catalog.LineItem;
import upcafe.model.catalog.ModifierData;
import upcafe.model.catalog.ModifierListData;
import upcafe.model.catalog.VariationData;
import upcafe.repository.catalog.CategoryRepository;
import upcafe.repository.catalog.ImageRepository;
// import upcafe.repository.catalog.ItemModifierListRepository;
import upcafe.repository.catalog.ItemRepository;
import upcafe.repository.catalog.ModifierListRepository;
import upcafe.repository.catalog.ModifierRepository;
import upcafe.repository.catalog.VariationRepository;

import upcafe.dto.catalog.*;


@Service
public class CatalogService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ModifierRepository modifierRepository;

    @Autowired
    private ModifierListRepository modListRepository;

    // @Autowired
    // private ItemModifierListRepository itemModListRepository;

    @Autowired
    private VariationRepository variationRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ImageRepository imageRepo;

    @Autowired
    private SquareClient client;

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
                                            .build();

                itemsDTO.add(menuItemDTO);
            });
        });

        return itemsDTO;
    }
    
    private List<ModifierListDTO> transferToListOfModifierListDTOs(Set<ModifierList> modifierListsDB) {
        List<ModifierListDTO> modifierListDTOs = new ArrayList<ModifierListDTO>();
        
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

        if(image != null) {

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
    
    public List<CategoryDTO> getCategories() {
        List<CategoryDTO> categories = new ArrayList<CategoryDTO>();
        categoryRepository.findAll().forEach(category -> {
            categories.add(new CategoryDTO.Builder().name(category.getName()).id(category.getId()).build());
        });

        return categories;
    }

                        // private List<VariationDTO> transferToListOfVariationDTOs(List<Variation> variationsDB) {
                    
                        //     List<VariationDTO> variationsDTO = new ArrayList<VariationDTO>();
                    
                        //     variationsDB.forEach(variation -> {
                    
                        //         VariationDTO variationDTO = new VariationDTO.Builder(variation.getId())
                        //             .name(variation.getName())
                        //             .inStock(variation.getInStock())
                        //             .price(variation.getPrice())
                        //             .image(transferToImageDTO(variation.getImage()))
                        //             .build();
                    
                        //         variationsDTO.add(variationDTO);
                        //     });
                    
                        //     return variationsDTO;
                        // }
    // public List<ModifierListDTO> getModifierLists() {

    //     List<ModifierListDTO> modifierListsDTO = new ArrayList<ModifierListDTO>();

    //     List<ModifierList> dbModifierLists = modListRepository.findAll();

    //     dbModifierLists.forEach(dbModifierList -> {
    //         ModifierListDTO modifierListDTO = new ModifierListDTO();
    //         modifierListDTO.setId(dbModifierList.getId());
    //         modifierListDTO.setName(dbModifierList.getName());
    //         modifierListDTO.setSelectionType(dbModifierList.getSelectionType());

    //         // TODO: change IMAGE to IMAGE_DTO
    //         modifierListDTO.setImage(dbModifierList.getImage());

    //         List<ModifierDTO> modifiersDTO = new ArrayList<ModifierDTO>();

    //         dbModifierList.getModifiers().forEach(dbModifier -> {
    //             ModifierDTO modifierDTO = new ModifierDTO();

    //             modifierDTO.setId(dbModifier.getId());

    //             // TODO: change IMAGE to IMAGE_DTO
    //             modifierDTO.setImage(dbModifier.getImage());

    //             modifierDTO.setName(dbModifier.getName());
    //             modifierDTO.setModifierListId(modifierListDTO.getId());
    //             modifierDTO.setInStock(dbModifier.isInStock());
    //             modifierDTO.setPrice(dbModifier.getPrice());
    //             modifierDTO.setOnByDefault(dbModifier.isOnByDefault());

    //             modifiersDTO.add(modifierDTO);
    //         });

    //         modifierListDTO.setModifiers(modifiersDTO);
    //         modifierListsDTO.add(modifierListDTO);
    //     });

    //     return modifierListsDTO;
    // }

    // public Iterable<ModifierDTO> getModifiers() {
    //     return modifierRepository.getModifiers();
    // }

    // public List<ImageDTO> getImages() {
    //     return imageRepo.getAllImages();
    // }
    // public void createImage(String catalogObjectId, String imageUrl, String
    // caption, String nameOfPicture) {
    //
    // CatalogImage requestImageImageData = new
    // CatalogImage.Builder().caption(caption).name(nameOfPicture).build();
    //
    // CatalogObject requestImage = new CatalogObject.Builder("IMAGE",
    // "#TEMP_ID").imageData(requestImageImageData)
    // .build();
    // CreateCatalogImageRequest request = new
    // CreateCatalogImageRequest.Builder(UUID.randomUUID().toString())
    // .objectId(catalogObjectId).image(requestImage).build();
    //
    // try {
    // client.getCatalogApi().createCatalogImage(request, new File(imageUrl));
    // } catch (ApiException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    //
    // public List<CatalogObject> test() throws ApiException, IOException {
    // return client.getCatalogApi().listCatalog(null, "IMAGE").getObjects();
    // }
    //
    // public List<Category> getCategories() {
    // return categoryRepository.findAll();
    // }
    //
    // public Catalog getCatalogByCategory(String category) {
    // Catalog catalog = new Catalog();
    // catalog.setCategory(category);
    //
    // List<Item> items = itemRepository.getItemsByCategoryName(category);
    // items.forEach(item -> {
    //
    // CategoryItem lineItem = new CategoryItem();
    //
    // lineItem.setItemData(getItemData(item));
    // lineItem.setVariationsData(getVariationsData(item.getItemId()));
    // lineItem.setModifierListsData(getModifierListsData(item.getItemId()));
    //
    // catalog.getItems().add(lineItem);
    // });
    //
    // return catalog;
    // }
    //
    // private List<ModifierListData> getModifierListsData(String itemId) {
    //
    //
    // List<ModifierListData> modifierListsData = new ArrayList<ModifierListData>();
    //
    // itemModListRepository.getItemModifierListsByItemId(itemId).forEach(itemModListId
    // -> {
    //
    // Optional<ModifierList> modList =
    // modListRepository.findById(itemModListId.getModifierListId());
    //
    // modList.ifPresent(dbList -> {
    // ModifierListData listData = new ModifierListData();
    // listData.setNameOfList(dbList.getName());
    //
    // if(dbList.getImage() != null) {
    // listData.setImageUrl(dbList.getImage().getUrl());
    // }
    //
    // listData.setSelectionType(dbList.getSelectionType());
    //
    // listData.setModifiers(getModifiersData(dbList.getId()));
    // modifierListsData.add(listData);
    // });
    // });
    //
    // return modifierListsData;
    // }
    //
    // private List<ModifierData> getModifiersData(String modListId) {
    //
    // List<ModifierData> modifiersData = new ArrayList<ModifierData>();
    //
    // modifierRepository.getModifiersByModListId(modListId).forEach(modifier -> {
    // ModifierData data = new ModifierData();
    // data.setId(modifier.getId());
    // data.setName(modifier.getName());
    // data.setOnByDefault(modifier.isOnByDefault());
    // data.setPrice(modifier.getPrice());
    //
    // modifiersData.add(data);
    // });
    //
    // return modifiersData;
    // }
    //
    // private List<VariationData> getVariationsData(String itemId) {
    // List<VariationData> variations = new ArrayList<VariationData>();
    //
    // variationRepository.getVariationsByItemItemId(itemId).forEach(variation -> {
    // VariationData varData = new VariationData();
    // varData.setName(variation.getName());
    // varData.setVariationId(variation.getVariationId());
    //
    // if(variation.getImage() != null)
    // varData.setVariationImageUrl(variation.getImage().getUrl());
    //
    // varData.setVariationPrice(variation.getPrice());
    //
    // variations.add(varData);
    // });
    //
    // return variations;
    // }
    //
    // private VariationData getSingleVariationData(ItemVariation variation) {
    // VariationData variationData = new VariationData();
    //
    // variationData.setName(variation.getName());
    // variationData.setVariationId(variation.getVariationId());
    //
    // if(variation.getImage() != null)
    // variationData.setVariationImageUrl(variation.getImage().getUrl());
    //
    // variationData.setVariationPrice(variation.getPrice());
    //
    // return variationData;
    // }
    //
    // private ItemData getItemData(Item item) {
    // ItemData itemData = new ItemData();
    // itemData.setName(item.getName());
    // itemData.setDescription(item.getDescription());
    // itemData.setImage(item.getImage());
    // return itemData;
    // }
    //
    // public LineItem getCategoryItemByVariationId(String id) {
    // LineItem lineItem = new LineItem();
    //
    // Optional<ItemVariation> variation = variationRepository.findById(id);
    //
    // lineItem.setVariationData(getSingleVariationData(variation.get()));
    // lineItem.setItemData(getItemData(variation.get().getItem()));
    // lineItem.setModifierListsData(getModifierListsData(variation.get().getItem().getItemId()));
    //
    // return lineItem;
    // }
    //
}
