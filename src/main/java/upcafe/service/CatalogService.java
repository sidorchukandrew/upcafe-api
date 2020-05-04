package upcafe.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import upcafe.entity.catalog.Item;
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
import upcafe.repository.catalog.ItemModifierListRepository;
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

    @Autowired
    private ItemModifierListRepository itemModListRepository;

    @Autowired
    private VariationRepository variationRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ImageRepository imageRepo;

    @Autowired
    private SquareClient client;

    public List<ItemDTO> getItemsForCategory(String category) {

        List<Item> items = itemRepository.getItemsByCategoryName(category);
        System.out.println("RETRIEVED");
        List<ItemDTO> itemsDTO = new ArrayList<ItemDTO>();
        
        items.forEach(item -> {

            List<VariationDTO> variationsDTO = new ArrayList<VariationDTO>();
            
            item.getVariations().forEach(variation -> {
                
                ImageDTO imageDTO = null;

                if(variation.getImage() != null) {

                    imageDTO = new ImageDTO.Builder()
                    .name(variation.getImage().getName())
                    .caption(variation.getImage().getCaption())
                    .url(variation.getImage().getUrl())
                    .build();
                }

                VariationDTO variationDTO = new VariationDTO.Builder(variation.getId())
                    .name(variation.getName())
                    .inStock(variation.getInStock())
                    .price(variation.getPrice())
                    .image(imageDTO)
                    .build(); 

                variationsDTO.add(variationDTO); 
            });

            ItemDTO itemDTO = new ItemDTO.Builder(item.getId())
                .description(item.getDescription())
                .name(item.getName())
                .variations(variationsDTO)
                .modifierLists(new ArrayList<ModifierListDTO>())
                .build();

            itemsDTO.add(itemDTO);
        });

        return itemsDTO;
    }

    public List<CategoryDTO> getCategories() {
        List<CategoryDTO> categories = new ArrayList<CategoryDTO>();
         categoryRepository.findAll().forEach(category -> {
             categories.add(new CategoryDTO.Builder()
                .name(category.getName())
                .id(category.getId())
                .build());
         });

         return categories;
    }
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
