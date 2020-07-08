package test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.squareup.square.models.CatalogImage;
import com.squareup.square.models.CatalogObject;

import upcafe.dto.catalog.CatalogDTO;
import upcafe.dto.catalog.ModifierDTO;
import upcafe.dto.catalog.ModifierListDTO;
import upcafe.dto.catalog.VariationDTO;
import upcafe.entity.catalog.Image;
import upcafe.entity.catalog.Item;
import upcafe.entity.catalog.Modifier;
import upcafe.entity.catalog.ModifierList;
import upcafe.entity.catalog.Variation;
import upcafe.error.NonExistentIdFoundException;
import upcafe.repository.catalog.ImageRepository;
import upcafe.repository.catalog.ItemRepository;
import upcafe.repository.catalog.ModifierListRepository;
import upcafe.repository.catalog.ModifierRepository;
import upcafe.repository.catalog.VariationRepository;
import upcafe.service.CatalogService;

@ExtendWith(MockitoExtension.class)
public class CatalogServiceTest {
	
	@InjectMocks
	private CatalogService catalogService;
	
	@Mock
	private ImageRepository imageRepo;
	
	@Mock
	private ModifierRepository modifierRepo;
	
	@Mock
	private ModifierListRepository modifierListRepo;
	
	@Mock
	private VariationRepository variationRepo;
	
	@Mock
	private ItemRepository itemRepo;
	
	@Test
	public void getCatalog_NonEmptyCatalog_Successful() {
		
		Modifier twentyOneSeasoning = new Modifier.Builder("TWENTY ONE SEASONING MODIFIER ID")
				.image(new Image.Builder("TWENTY ONE SEASONING MODIFIER IMAGE ID").url("https://www.google.com").build())
				.inStock(true)
				.name("Twenty One Seasoning")
				.onByDefault(false)
				.price(0)
				.modifierList(new ModifierList.Builder("SEASONINGS MOD LIST ID").build())
				.build();
		
		Modifier everythingSeasoning = new Modifier.Builder("EVERYTHING SEASONING MODIFIER ID")
				.image(new Image.Builder("EVERYTHING SEASONING MODIFIER IMAGE ID").url("https://www.google.com").build())
				.inStock(true)
				.name("Everything But the Bagael Seasoning")
				.onByDefault(false)
				.price(0)
				.modifierList(new ModifierList.Builder("SEASONINGS MOD LIST ID").build())
				.build();
		
		
		ModifierList seasonings = new ModifierList.Builder("SEASONINGS MOD LIST ID")
				.modifiers(Arrays.asList(everythingSeasoning, twentyOneSeasoning))
				.selectionType("SINGLE")
				.name("Seasonings")
				.image(new Image.Builder("SEASONINGS IMAGE").url("https://www.google.com").build())
				.build();
		
		
		Modifier wholeWheatBread = new Modifier.Builder("WHOLE WHEAT BREAD MODIFIER ID")
				.image(new Image.Builder("WHOLE WHEAT BREAD MODIFIER IMAGE ID").url("https://www.google.com").build())
				.inStock(true)
				.name("Whole Wheat Bread")
				.onByDefault(false)
				.price(0)
				.modifierList(new ModifierList.Builder("BREADS MOD LIST ID").build())
				.build();
		
		Modifier italianBread = new Modifier.Builder("ITALIAN BREAD MODIFIER ID")
				.image(new Image.Builder("ITALIAN BREAD SEASONING MODIFIER IMAGE ID").url("https://www.google.com").build())
				.inStock(true)
				.name("Italian Bread")
				.onByDefault(true)
				.price(0)
				.modifierList(new ModifierList.Builder("BREADS MOD LIST ID").build())
				.build();
		
		ModifierList toastBreads = new ModifierList.Builder("BREADS MOD LIST ID")
				.modifiers(Arrays.asList(wholeWheatBread, italianBread))
				.selectionType("SINGLE")
				.name("Breads")
				.image(new Image.Builder("BREADS IMAGE").url("https://www.google.com").build())
				.build();
		
		Variation avocadoVariation = new Variation.Builder("AVOCADO TOAST VARIATION ID")
				.name("Regular")
				.inStock(true)
				.price(2)
				.image(new Image.Builder("AVOCADO TOAST IMAGE ID").url("https://www.google.com").build())
				.build();
		
		Item avocadoToast = new Item.Builder("AVOCADO_TOAST_ID")
				.description("Delicious avocado spread over toast and seasoned with Everything Seasoning")
				.variations(Arrays.asList(avocadoVariation))
				.modifierLists(new HashSet<>(Arrays.asList(seasonings, toastBreads)))
				.name("Avocado Toast")
				.build();
		
		Variation strawberryLemonade = new Variation.Builder("STRAWBERRY LEMONADE VARIATION ID")
				.name("Strawberry Lemonade")
				.inStock(true)
				.price(1.5)
				.image(new Image.Builder("STRAWBERRY LEMONADE IMAGE ID").url("https://www.google.com").build())
				.build();
		
		Variation regularLemonade = new Variation.Builder("LEMONADE VARIATION ID")
				.name("Regular")
				.inStock(true)
				.price(1.5)
				.image(new Image.Builder("LEMONADE IMAGE ID").url("https://www.google.com").build())
				.build();
		
		Item lemonade = new Item.Builder("LEMONADE ID")
				.description("Cool, refreshing lemonade with your choice of flavor.")
				.modifierLists(new HashSet<>())
				.variations(Arrays.asList(strawberryLemonade, regularLemonade))
				.name("Lemonade")
				.build();
		
		
		when(itemRepo.findAll()).thenReturn(Arrays.asList(avocadoToast, lemonade));
		
		CatalogDTO catalog = catalogService.getCatalog();
		
		assertEquals(3, catalog.getItemsList().size());
		assertEquals(2, catalog.getModifierLists().size());

		VariationDTO lemonadeDTO = null;
		for(VariationDTO item : catalog.getItemsList()) {
			if(item.getId().compareTo("LEMONADE VARIATION ID") == 0) {
				lemonadeDTO = item;
				break;
			}
		}
		
		assertNotNull(lemonadeDTO);
		assertEquals("Lemonade", lemonadeDTO.getName());
		assertEquals(1.5, lemonadeDTO.getPrice());
		assertTrue(lemonadeDTO.isInStock());
		assertEquals("https://www.google.com", lemonadeDTO.getImage().getUrl());
		
		VariationDTO strawberryLemonadeDTO = null;
		for(VariationDTO item : catalog.getItemsList()) {
			if(item.getId().compareTo("STRAWBERRY LEMONADE VARIATION ID") == 0) {
				strawberryLemonadeDTO = item;
				break;
			}
		}
		
		assertNotNull(strawberryLemonadeDTO);
		assertEquals("Strawberry Lemonade", strawberryLemonadeDTO.getName());
		assertEquals(1.5, strawberryLemonadeDTO.getPrice());
		assertTrue(strawberryLemonadeDTO.isInStock());
		assertEquals("https://www.google.com", strawberryLemonadeDTO.getImage().getUrl());
		
		VariationDTO avocadoToastDTO = null;
		for(VariationDTO item : catalog.getItemsList()) {
			if(item.getId().compareTo("AVOCADO TOAST VARIATION ID") == 0) {
				avocadoToastDTO = item;
				break;
			}
		}
		
		assertNotNull(avocadoToastDTO);
		assertEquals("Avocado Toast", avocadoToastDTO.getName());
		assertEquals(2, avocadoToastDTO.getPrice());
		assertTrue(avocadoToastDTO.isInStock());
		assertEquals("https://www.google.com", avocadoToastDTO.getImage().getUrl());
		
		ModifierListDTO toastBreadsDTO = null;
		for(ModifierListDTO modifierList : catalog.getModifierLists()) {
			if(modifierList.getId().compareTo("BREADS MOD LIST ID") == 0) {
				toastBreadsDTO = modifierList;
				break;
			}
		}
		
		assertNotNull(toastBreadsDTO);
		assertEquals("Breads", toastBreadsDTO.getName());
		assertEquals("https://www.google.com", toastBreadsDTO.getImage().getUrl());
		assertEquals("SINGLE", toastBreadsDTO.getSelectionType());
		assertEquals(2, toastBreads.getModifiers().size());
		
		ModifierDTO italianBreadDTO = null;
		
		for(ModifierDTO modifier : toastBreadsDTO.getModifiers()) {
			if(modifier.getId().compareTo("ITALIAN BREAD MODIFIER ID") == 0) {
				italianBreadDTO = modifier;
				break;
			}
		}
		
		assertNotNull(italianBreadDTO);
		assertEquals("Italian Bread", italianBreadDTO.getName());
		assertTrue(italianBreadDTO.getOnByDefault());
		assertTrue(italianBreadDTO.isInStock());
		assertEquals("https://www.google.com", italianBreadDTO.getImage().getUrl());
		assertEquals("BREADS MOD LIST ID", italianBreadDTO.getModifierListId());
		assertEquals(0, italianBreadDTO.getPrice());
		
		ModifierDTO wholeWheatBreadDTO = null;
		
		for(ModifierDTO modifier : toastBreadsDTO.getModifiers()) {
			if(modifier.getId().compareTo("WHOLE WHEAT BREAD MODIFIER ID") == 0) {
				wholeWheatBreadDTO = modifier;
				break;
			}
		}
		
		assertNotNull(wholeWheatBreadDTO);
		assertEquals("Whole Wheat Bread", wholeWheatBreadDTO.getName());
		assertFalse(wholeWheatBreadDTO.getOnByDefault());
		assertTrue(wholeWheatBreadDTO.isInStock());
		assertEquals("https://www.google.com", wholeWheatBreadDTO.getImage().getUrl());
		assertEquals("BREADS MOD LIST ID", wholeWheatBreadDTO.getModifierListId());
		assertEquals(0, wholeWheatBreadDTO.getPrice());
	}
	
	@Test
	public void getCatalog_EmptyCatalog_Successful() {
		
		when(itemRepo.findAll()).thenReturn(new ArrayList<Item>());
		
		
		CatalogDTO catalog = catalogService.getCatalog();
		
		assertEquals(0, catalog.getItemsList().size());
		assertEquals(0, catalog.getModifierLists().size());
	}
	
	@Test
	public void getSquareCatalogObjectById_EmptyStringPassed_ExceptionThrown() {
	
		assertThrows(NonExistentIdFoundException.class, () -> catalogService.getSquareCatalogObjectById(""));
	}
	
	@Test
	public void getSquareCatalogObjectById_NullIdPassed_ExceptionThrown() {
	
		assertThrows(NonExistentIdFoundException.class, () -> catalogService.getSquareCatalogObjectById(null));
	}
	
	@Test
	public void assignImageToObjectLocally_VariationImage_CalledOnce() {
		CatalogObject variation = new CatalogObject.Builder("ITEM_VARIATION", "VARIATION ID 1")
				.imageId("IMAGE ID 1")
				.build();
		
		catalogService.assignImageToObjectLocally(variation);
		Mockito.verify(variationRepo).assignImageToVariation(Mockito.anyString(), Mockito.anyString());
	}
	
	@Test
	public void assignImageToObjectLocally_ModifierListImage_CalledOnce() {
		CatalogObject modifierList = new CatalogObject.Builder("MODIFIER_LIST", "MODIFIER LIST ID 1")
				.imageId("IMAGE ID 1")
				.build();
		
		catalogService.assignImageToObjectLocally(modifierList);
		Mockito.verify(modifierListRepo).assignImageToModifierList(Mockito.anyString(), Mockito.anyString());
	}
	
	@Test
	public void assignImageToObjectLocally_ModifierImage_CalledOnce() {
		CatalogObject modifier = new CatalogObject.Builder("MODIFIER", "MODIFIER ID 1")
				.imageId("IMAGE ID 1")
				.build();
		
		catalogService.assignImageToObjectLocally(modifier);
		Mockito.verify(modifierRepo).assignImageToModifier(Mockito.anyString(), Mockito.anyString());
	}
	
	@Test
	public void assignImageToObjectLocally_UnsupportedType_ExceptionThrown() {
		CatalogObject objectWithImage = new CatalogObject.Builder("CATEGORY", "CATEGORY ID 1").build();
		
		assertThrows(UnsupportedOperationException.class, () -> catalogService.assignImageToObjectLocally(objectWithImage));
	}
	
	@Test
	public void saveImageLocally_AllParametersPresent_Successful() {
		CatalogImage imageData = new CatalogImage.Builder()
				.caption("This is a test caption")
				.name("Test Image")
				.url("https://www.google.com")
				.build();
		
		CatalogObject squareImage = new CatalogObject.Builder("IMAGE", "IMAGE_ID_1")
				.imageData(imageData)
				.build();
		
		when(imageRepo.save(Mockito.any(Image.class))).thenReturn(new Image.Builder("IMAGE_ID_1")
				.batchUpdateId("BATCH UPDATE ID 1")
				.lastUpdated(LocalDateTime.now())
				.caption("This is a test caption")
				.name("Test Image")
				.url("https://www.google.com")
				.build());
		
		Image savedImage = catalogService.saveImageLocally(squareImage);
		
		assertNotNull(savedImage);
		
		assertEquals("This is a test caption", savedImage.getCaption());
		assertEquals("Test Image", savedImage.getName());
		assertEquals("https://www.google.com", savedImage.getUrl());
	}
	
	@Test
	public void saveImageLocally_NullPassed_ExceptionThrown() {
		assertThrows(NonExistentIdFoundException.class, () -> catalogService.saveImageLocally(null));
	}

}
