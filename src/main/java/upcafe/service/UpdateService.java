package upcafe.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import com.squareup.square.Environment;
import com.squareup.square.SquareClient;
import com.squareup.square.exceptions.ApiException;
import com.squareup.square.models.CatalogItemModifierListInfo;
import com.squareup.square.models.CatalogModifierList;
import com.squareup.square.models.CatalogObject;
import com.squareup.square.models.ListCatalogResponse;

import upcafe.entity.catalog.Category;
import upcafe.entity.catalog.Image;
import upcafe.entity.catalog.Item;
import upcafe.entity.catalog.Variation;
import upcafe.entity.catalog.Modifier;
import upcafe.entity.catalog.ModifierList;
import upcafe.repository.catalog.CategoryRepository;
import upcafe.repository.catalog.ImageRepository;
import upcafe.repository.catalog.ItemRepository;
import upcafe.repository.catalog.ModifierListRepository;
import upcafe.repository.catalog.ModifierRepository;
import upcafe.repository.catalog.VariationRepository;

@Configuration
@EnableScheduling
public class UpdateService {

    private static final String IMAGE = "IMAGE";
    private static final String MODIFIER_LIST = "MODIFIER_LIST";
    private static final String ITEM = "ITEM";
    private static final String CATEGORY = "CATEGORY";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";

    private final double SMALLEST_CURRENCY_DENOMINATOR = 100;

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
    // @Autowired
    // private ItemModifierListRepository itemModListRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
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

            // itemModListRepository.deleteOldBatchUpdateIds(batchUpdateId);
            variationRepository.deleteOldBatchUpdateIds(batchUpdateId);
            itemRepository.deleteOldBatchUpdateIds(batchUpdateId);

            response = retrieveFromSquare(CATEGORY);

            if (response.getObjects() != null) {
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

        } catch (IOException e) {
            System.out.println("IO EXCEPTION : ");
            e.printStackTrace();
            System.out.println(" - - - - - - - - - - - - Attempting update again  - - - - - - - - - - - - ");
            updateLocalCatalog();
        } catch (ApiException e) {
            System.out.println("API EXCEPTION : ");
            e.printStackTrace();
        }
    }

    private ListCatalogResponse retrieveFromSquare(String type) throws ApiException, IOException {
        return client().getCatalogApi().listCatalog(null, type);
    }

    public Iterable<ModifierList> getModifierLists() {
        return modListRepository.findAll();
    }

    private void saveCategory(CatalogObject categorySquare, String batchUpdateId) {

        Optional<Category> optCategory = categoryRepository.findById(categorySquare.getId());

        LocalDateTime updatedAtInSquare = parseDateAndTimeFromSquare(categorySquare.getUpdatedAt());

        if (!optCategory.isPresent()) {

            Category categoryLocal = new Category.Builder(categorySquare.getId())
                .name(categorySquare.getCategoryData().getName())
                .lastUpdated(updatedAtInSquare)
                .batchUpdateId(batchUpdateId)
                .build();

            categoryRepository.save(categoryLocal);
        } else {

            Category categoryLocal = optCategory.get();

            if (categoryLocal.getLastUpdated() == null) {
                categoryLocal.setBatchUpdateId(batchUpdateId);
                categoryLocal.setName(categorySquare.getCategoryData().getName());

                categoryLocal.setLastUpdated(updatedAtInSquare);

            } else if (!categoryLocal.getLastUpdated().isEqual(updatedAtInSquare)) {
                categoryLocal.setBatchUpdateId(batchUpdateId);
                categoryLocal.setName(categorySquare.getCategoryData().getName());
                categoryLocal.setLastUpdated(updatedAtInSquare);
            } else {
                categoryLocal.setBatchUpdateId(batchUpdateId);
            }

            categoryRepository.save(categoryLocal);
        }
    }

    private void saveModifierList(CatalogObject catalogObjectSquare, String batchUpdateId) {

        Optional<ModifierList> optModList = modListRepository.findById(catalogObjectSquare.getId());
        LocalDateTime lastUpdatedInSquare = parseDateAndTimeFromSquare(catalogObjectSquare.getUpdatedAt());
        ModifierList modifierListLocal;

        if (!optModList.isPresent()) {

            CatalogModifierList modifierListSquare = catalogObjectSquare.getModifierListData();

            modifierListLocal = new ModifierList.Builder(catalogObjectSquare.getId())
                                .name(modifierListSquare.getName())
                                .selectionType(modifierListSquare.getSelectionType())
                                .batchUpdateId(batchUpdateId)
                                .lastUpdated(lastUpdatedInSquare)
                                .image(new Image.Builder(catalogObjectSquare.getImageId()).build())
                                .build();

            modListRepository.save(modifierListLocal);
        }

        else {

            modifierListLocal = optModList.get();

            if (!modifierListLocal.getLastUpdated().isEqual(lastUpdatedInSquare)) {
                modifierListLocal.setBatchUpdateId(batchUpdateId);
                modifierListLocal.setLastUpdated(lastUpdatedInSquare);

                modifierListLocal.setName(catalogObjectSquare.getModifierListData().getName());
                modifierListLocal.setSelectionType(catalogObjectSquare.getModifierListData().getSelectionType());

                if (catalogObjectSquare.getImageId() != null)
                    modifierListLocal
                            .setImage(new Image.Builder(catalogObjectSquare.getImageId()).build());
            } else {
                modifierListLocal.setBatchUpdateId(batchUpdateId);
            }

            modListRepository.save(modifierListLocal);
        }

        if (catalogObjectSquare.getModifierListData().getModifiers() != null) {
            catalogObjectSquare.getModifierListData().getModifiers().forEach(modifier -> {
                saveModifier(modifier, modifierListLocal, batchUpdateId);
            });
        }
    }

    private LocalDateTime parseDateAndTimeFromSquare(String dateTimeSquare) {
        ZonedDateTime square = ZonedDateTime.parse(dateTimeSquare);
        square = square.minusNanos(square.getNano());
        ZonedDateTime zonedTime = square.withZoneSameLocal(ZoneId.of("America/New_York")).minusHours(4);

        return zonedTime.toLocalDateTime();
    }

    private void saveModifier(CatalogObject modifierSquare, ModifierList modList, String batchUpdateId) {

        Optional<Modifier> optModifier = modifierRepository.findById(modifierSquare.getId());
        LocalDateTime lastUpdatedInSquare = parseDateAndTimeFromSquare(modifierSquare.getUpdatedAt());

        if (!optModifier.isPresent()) {

            Modifier modifierLocal = new Modifier.Builder(modifierSquare.getId())
                .batchUpdateId(batchUpdateId)
                .lastUpdated(lastUpdatedInSquare)
                .modifierList(modList)
                .onByDefault(true)
                .name(modifierSquare.getModifierData().getName())
                .build();

            if (modifierSquare.getModifierData().getPriceMoney() != null) {
                if (modifierSquare.getModifierData().getPriceMoney().getCurrency().compareTo("USD") == 0) {
                    modifierLocal.setPrice(modifierSquare.getModifierData().getPriceMoney().getAmount()
                            / SMALLEST_CURRENCY_DENOMINATOR);
                }
            }

            modifierRepository.save(modifierLocal);
        } else {

            Modifier modifierLocal = optModifier.get();

            if (!modifierLocal.getLastUpdated().isEqual(lastUpdatedInSquare)) {

                modifierLocal.setLastUpdated(lastUpdatedInSquare);
                modifierLocal.setBatchUpdateId(batchUpdateId);

                modifierLocal.setModifierList(modList);
                modifierLocal.setName(modifierSquare.getModifierData().getName());

                if (modifierSquare.getModifierData().getPriceMoney() != null) {
                    if (modifierSquare.getModifierData().getPriceMoney().getCurrency().compareTo("USD") == 0) {
                        modifierLocal.setPrice(modifierSquare.getModifierData().getPriceMoney().getAmount()
                                / SMALLEST_CURRENCY_DENOMINATOR);
                    }
                }
            }

            else {
                modifierLocal.setBatchUpdateId(batchUpdateId);
            }

            modifierRepository.save(modifierLocal);
        }
    }

    private void saveImage(CatalogObject imageSquare, String batchUpdateId) {

        Optional<Image> optImage = imageRepository.findById(imageSquare.getId());

        LocalDateTime updatedAtInSquare = parseDateAndTimeFromSquare(imageSquare.getUpdatedAt());

        if (!optImage.isPresent()) {
            Image imageLocal = new Image.Builder(imageSquare.getId())
                                .name(imageSquare.getImageData().getName())
                                .url(imageSquare.getImageData().getUrl())
                                .caption(imageSquare.getImageData().getCaption())
                                .batchUpdateId(batchUpdateId)
                                .build();

            imageRepository.save(imageLocal);
        } else {

            Image imageLocal = optImage.get();

            if (imageLocal.getLastUpdated() == null) {
                imageLocal.setBatchUpdateId(batchUpdateId);
                imageLocal.setCaption(imageSquare.getImageData().getCaption());
                imageLocal.setName(imageSquare.getImageData().getName());
                imageLocal.setUrl(imageSquare.getImageData().getUrl());

                imageLocal.setLastUpdated(updatedAtInSquare);

            } else if (!imageLocal.getLastUpdated().isEqual(updatedAtInSquare)) {
                imageLocal.setBatchUpdateId(batchUpdateId);
                imageLocal.setCaption(imageSquare.getImageData().getCaption());
                imageLocal.setName(imageSquare.getImageData().getName());
                imageLocal.setUrl(imageSquare.getImageData().getUrl());

                imageLocal.setLastUpdated(updatedAtInSquare);
            } else {
                imageLocal.setBatchUpdateId(batchUpdateId);
            }

            imageRepository.save(imageLocal);
        }
    }

    private void saveItem(CatalogObject itemSquare, String batchUpdateId) {

        Optional<Item> optItem = itemRepository.findById(itemSquare.getId());

        LocalDateTime updatedAtInSquare = parseDateAndTimeFromSquare(itemSquare.getUpdatedAt());
        
        // Create a new item and save it
        if (!optItem.isPresent()) {

            Set<ModifierList> modifierLists = new HashSet<ModifierList>();

            if(itemSquare.getItemData().getModifierListInfo() != null) {

                itemSquare.getItemData().getModifierListInfo().forEach(modListInfoPacket -> {
                    modifierLists.add(new ModifierList.Builder(modListInfoPacket.getModifierListId()).build());
                });
            }

            Item item = new Item.Builder(itemSquare.getId())
                .description(itemSquare.getItemData().getDescription())
                .name(itemSquare.getItemData().getName())
                .batchUpdateId(batchUpdateId)
                .lastUpdated(updatedAtInSquare)
                .category(new Category.Builder(itemSquare.getItemData().getCategoryId()).build())
                .modifierLists(modifierLists)
                .build();

            itemRepository.save(item);

            itemSquare.getItemData().getVariations().forEach(variationObject -> {
                saveVariation(variationObject, batchUpdateId);
            });
        }

        // The item already exists in the DB, it's not a new item
        else {

            Item localItem = optItem.get();

            // If the item has been updated in Square, update it locally
            if (!localItem.getLastUpdated().isEqual(updatedAtInSquare)) {

                Set<ModifierList> modifierLists = new HashSet<ModifierList>();

                if (itemSquare.getItemData().getModifierListInfo() != null) {

                    itemSquare.getItemData().getModifierListInfo().forEach(modListInfoPacket -> {
                        modifierLists.add(new ModifierList.Builder(modListInfoPacket.getModifierListId()).build());
                    });
                }

                localItem.setId(itemSquare.getId());
                localItem.setDescription(itemSquare.getItemData().getDescription());
                localItem.setName(itemSquare.getItemData().getName());
                localItem.setCategory(new Category.Builder(itemSquare.getItemData().getCategoryId()).build());
                localItem.setBatchUpdateId(batchUpdateId);
                localItem.setModifierLists(modifierLists);
                localItem.setLastUpdated(updatedAtInSquare);

                itemRepository.save(localItem);
            }

            // Just update the batch id
            else {
                localItem.setBatchUpdateId(batchUpdateId);
                itemRepository.save(localItem);
            }

            itemSquare.getItemData().getVariations().forEach(variationObject -> {
                saveVariation(variationObject, batchUpdateId);
            });
        }
    }


    private void saveVariation(CatalogObject variationSquare, String batchUpdateId) {

        Optional<Variation> optVariation = variationRepository.findById(variationSquare.getId());
        LocalDateTime updatedAtInSquare = parseDateAndTimeFromSquare(variationSquare.getUpdatedAt());

        if (!optVariation.isPresent()) {

            Variation variation = new Variation();
            Item item = new Item();

            item.setId(variationSquare.getItemVariationData().getItemId());
            variation.setName(variationSquare.getItemVariationData().getName());

            if (variationSquare.getItemVariationData().getPriceMoney() != null)
                variation.setPrice(variationSquare.getItemVariationData().getPriceMoney().getAmount()
                        / SMALLEST_CURRENCY_DENOMINATOR);
            variation.setItem(item);
            variation.setId(variationSquare.getId());
            variation.setBatchUpdateId(batchUpdateId);

            variation.setUpdatedAt(updatedAtInSquare);

            if (variationSquare.getImageId() != null)
                variation.setImage(new Image.Builder(variationSquare.getImageId()).build());

            System.out.println(variation.toString());

            variationRepository.save(variation);
        }
        // Item exists, so we need to check if its been updated
        else {

            Variation variationLocal = optVariation.get();

            if (!variationLocal.getUpdatedAt().isEqual(updatedAtInSquare)) {

                Item item = new Item();

                item.setId(variationSquare.getItemVariationData().getItemId());
                variationLocal.setName(variationSquare.getItemVariationData().getName());
                variationLocal.setPrice(variationSquare.getItemVariationData().getPriceMoney().getAmount()
                        / SMALLEST_CURRENCY_DENOMINATOR);
                variationLocal.setItem(item);
                variationLocal.setId(variationSquare.getId());
                variationLocal.setBatchUpdateId(batchUpdateId);

                variationLocal.setUpdatedAt(updatedAtInSquare);

                if (variationSquare.getImageId() != null)
                    variationLocal.setImage(new Image.Builder(variationSquare.getImageId()).build());
            } else {
                variationLocal.setBatchUpdateId(batchUpdateId);
            }

            variationRepository.save(variationLocal);
        }
    }
}
