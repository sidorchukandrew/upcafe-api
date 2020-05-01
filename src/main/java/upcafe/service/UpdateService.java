package upcafe.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
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
import upcafe.entity.catalog.ItemModListKey;
import upcafe.entity.catalog.ItemModifierList;
import upcafe.entity.catalog.Variation;
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

            Category categoryLocal = new Category();

            categoryLocal.setId(categorySquare.getId());
            categoryLocal.setName(categorySquare.getCategoryData().getName());
            categoryLocal.setBatchUpdateId(batchUpdateId);

            categoryLocal.setUpdatedAt(updatedAtInSquare);

            categoryRepository.save(categoryLocal);
        } else {

            Category categoryLocal = optCategory.get();

            if (categoryLocal.getUpdatedAt() == null) {
                categoryLocal.setBatchUpdateId(batchUpdateId);
                categoryLocal.setName(categorySquare.getCategoryData().getName());

                categoryLocal.setUpdatedAt(updatedAtInSquare);

            } else if (!categoryLocal.getUpdatedAt().isEqual(updatedAtInSquare)) {
                categoryLocal.setBatchUpdateId(batchUpdateId);
                categoryLocal.setName(categorySquare.getCategoryData().getName());
                categoryLocal.setUpdatedAt(updatedAtInSquare);
            } else {
                categoryLocal.setBatchUpdateId(batchUpdateId);
            }

            categoryRepository.save(categoryLocal);
        }
    }

    private void saveModifierList(CatalogObject catalogObjectSquare, String batchUpdateId) {

        Optional<ModifierList> optModList = modListRepository.findById(catalogObjectSquare.getId());

        if (!optModList.isPresent()) {

            CatalogModifierList modifierListSquare = catalogObjectSquare.getModifierListData();

            ModifierList modifierListLocal = new ModifierList();
            modifierListLocal.setId(catalogObjectSquare.getId());
            modifierListLocal.setName(modifierListSquare.getName());
            modifierListLocal.setSelectionType(modifierListSquare.getSelectionType());
            modifierListLocal.setBatchUpdateId(batchUpdateId);

            LocalDateTime updatedAt = parseDateAndTimeFromSquare(catalogObjectSquare.getUpdatedAt());
            modifierListLocal.setUpdatedAt(updatedAt);

            if (catalogObjectSquare.getImageId() != null)
                modifierListLocal.setImage(new Image(catalogObjectSquare.getImageId(), null, null, null, null, null));

            System.out.println("\n" + modifierListLocal.toString() + "\n");

            modListRepository.save(modifierListLocal);

            catalogObjectSquare.getModifierListData().getModifiers().forEach(modifier -> {
                saveModifier(modifier, modifierListLocal, batchUpdateId);
            });
        }

        else {
            if (!optModList.get().getUpdatedAt()
                    .isEqual(parseDateAndTimeFromSquare(catalogObjectSquare.getUpdatedAt()))) {
                optModList.get().setBatchUpdateId(batchUpdateId);

                LocalDateTime updatedAt = parseDateAndTimeFromSquare(catalogObjectSquare.getUpdatedAt());
                optModList.get().setUpdatedAt(updatedAt);

                optModList.get().setName(catalogObjectSquare.getModifierListData().getName());
                optModList.get().setSelectionType(catalogObjectSquare.getModifierListData().getSelectionType());

                if (catalogObjectSquare.getImageId() != null)
                    optModList.get()
                            .setImage(new Image(catalogObjectSquare.getImageId(), null, null, null, null, null));
            } else {
                optModList.get().setBatchUpdateId(batchUpdateId);
            }

            modListRepository.save(optModList.get());

            catalogObjectSquare.getModifierListData().getModifiers().forEach(m -> {
                saveModifier(m, optModList.get(), batchUpdateId);
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

        if (!optModifier.isPresent()) {

            Modifier modifierLocal = new Modifier();
            modifierLocal.setBatchUpdateId(batchUpdateId);

            LocalDateTime updatedAt = parseDateAndTimeFromSquare(modifierSquare.getUpdatedAt());
            modifierLocal.setUpdatedAt(updatedAt);
            modifierLocal.setId(modifierSquare.getId());
            modifierLocal.setModifierList(modList);

            if (modifierSquare.getModifierData().getPriceMoney() != null) {
                if (modifierSquare.getModifierData().getPriceMoney().getCurrency().compareTo("USD") == 0) {
                    modifierLocal.setPrice(modifierSquare.getModifierData().getPriceMoney().getAmount()
                            / SMALLEST_CURRENCY_DENOMINATOR);
                }
            }

            modifierLocal.setOnByDefault(false);
            modifierLocal.setName(modifierSquare.getModifierData().getName());

            modifierRepository.save(modifierLocal);
            System.out.println("\t" + modifierLocal.toString());
        } else {
            if (!optModifier.get().getUpdatedAt().isEqual(parseDateAndTimeFromSquare(modifierSquare.getUpdatedAt()))) {

                LocalDateTime updatedAt = parseDateAndTimeFromSquare(modifierSquare.getUpdatedAt());
                optModifier.get().setUpdatedAt(updatedAt);
                optModifier.get().setBatchUpdateId(batchUpdateId);

                optModifier.get().setModifierList(modList);
                optModifier.get().setName(modifierSquare.getModifierData().getName());

                if (modifierSquare.getModifierData().getPriceMoney() != null) {
                    if (modifierSquare.getModifierData().getPriceMoney().getCurrency().compareTo("USD") == 0) {
                        optModifier.get().setPrice(modifierSquare.getModifierData().getPriceMoney().getAmount()
                                / SMALLEST_CURRENCY_DENOMINATOR);
                    }
                }
            }

            else {
                optModifier.get().setBatchUpdateId(batchUpdateId);
            }

            modifierRepository.save(optModifier.get());
        }
    }

    private void saveImage(CatalogObject imageSquare, String batchUpdateId) {

        Optional<Image> optImage = imageRepository.findById(imageSquare.getId());

        LocalDateTime updatedAtInSquare = parseDateAndTimeFromSquare(imageSquare.getUpdatedAt());

        if (!optImage.isPresent()) {
            Image imageLocal = new Image();
            imageLocal.setId(imageSquare.getId());
            imageLocal.setName(imageSquare.getImageData().getName());
            imageLocal.setUrl(imageSquare.getImageData().getUrl());
            imageLocal.setCaption(imageSquare.getImageData().getCaption());

            imageLocal.setUpdatedAt(updatedAtInSquare);
            imageLocal.setBatchUpdateId(batchUpdateId);

            imageRepository.save(imageLocal);
        } else {

            Image imageLocal = optImage.get();

            if (imageLocal.getUpdatedAt() == null) {
                imageLocal.setBatchUpdateId(batchUpdateId);
                imageLocal.setCaption(imageSquare.getImageData().getCaption());
                imageLocal.setName(imageSquare.getImageData().getName());
                imageLocal.setUrl(imageSquare.getImageData().getUrl());

                imageLocal.setUpdatedAt(updatedAtInSquare);

            } else if (!imageLocal.getUpdatedAt().isEqual(updatedAtInSquare)) {
                imageLocal.setBatchUpdateId(batchUpdateId);
                imageLocal.setCaption(imageSquare.getImageData().getCaption());
                imageLocal.setName(imageSquare.getImageData().getName());
                imageLocal.setUrl(imageSquare.getImageData().getUrl());

                imageLocal.setUpdatedAt(updatedAtInSquare);
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
            Item item = new Item();

            item.setId(itemSquare.getId());
            item.setDescription(itemSquare.getItemData().getDescription());
            item.setName(itemSquare.getItemData().getName());
            item.setCategory(new Category(itemSquare.getItemData().getCategoryId(), null, null, null, null));
            item.setBatchUpdateId(batchUpdateId);

            item.setUpdatedAt(updatedAtInSquare);

            itemRepository.save(item);

            itemSquare.getItemData().getVariations().forEach(variationObject -> {
                saveVariation(variationObject, batchUpdateId);
            });

            if (itemSquare.getItemData().getModifierListInfo() != null) {
                itemSquare.getItemData().getModifierListInfo().forEach(modifierListInfo -> {
                    saveItemModList(itemSquare, modifierListInfo, batchUpdateId);
                });
            }
        }

        // The item already exists in the DB, it's not a new item
        else {

            Item localItem = optItem.get();

            // If the item has been updated in Square, update it locally
            if (!localItem.getUpdatedAt().isEqual(updatedAtInSquare)) {
                localItem.setId(itemSquare.getId());
                localItem.setDescription(itemSquare.getItemData().getDescription());
                localItem.setName(itemSquare.getItemData().getName());
                localItem.setCategory(new Category(itemSquare.getItemData().getCategoryId(), null, null, null, null));
                localItem.setBatchUpdateId(batchUpdateId);

                localItem.setUpdatedAt(updatedAtInSquare);

                itemRepository.save(localItem);
                System.out.println(optItem.get().toString());
            }

            // Just update the batch id
            else {
                localItem.setBatchUpdateId(batchUpdateId);
                itemRepository.save(localItem);
            }

            itemSquare.getItemData().getVariations().forEach(variationObject -> {
                saveVariation(variationObject, batchUpdateId);
            });

            if (itemSquare.getItemData().getModifierListInfo() != null) {
                itemSquare.getItemData().getModifierListInfo().forEach(modifierListInfo -> {
                    saveItemModList(itemSquare, modifierListInfo, batchUpdateId);
                });
            }
        }
    }

    private void saveItemModList(CatalogObject itemObject, CatalogItemModifierListInfo modList, String batchUpdateId) {

        ItemModifierList itemModifierList = new ItemModifierList();

        ItemModListKey key = new ItemModListKey();

        key.setBatchUpdateId(batchUpdateId);
        key.setItemId(itemObject.getId());
        key.setModifierListId(modList.getModifierListId());

        itemModifierList.setId(key);

        itemModListRepository.save(itemModifierList);

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
                variation.setImage(new Image(variationSquare.getImageId(), null, null, null, null, null));

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
                    variationLocal.setImage(new Image(variationSquare.getImageId(), null, null, null, null, null));
            } else {
                variationLocal.setBatchUpdateId(batchUpdateId);
            }

            variationRepository.save(variationLocal);
        }
    }
}
