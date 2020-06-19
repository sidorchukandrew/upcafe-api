package test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import upcafe.dto.catalog.ModifierListDTO;
import upcafe.dto.menu.MenuDTO;
import upcafe.dto.menu.MenuItemDTO;
import upcafe.entity.catalog.Category;
import upcafe.entity.catalog.Image;
import upcafe.entity.catalog.Item;
import upcafe.entity.catalog.Modifier;
import upcafe.entity.catalog.ModifierList;
import upcafe.entity.catalog.Variation;
import upcafe.error.NonExistentIdFoundException;
import upcafe.repository.catalog.CategoryRepository;
import upcafe.repository.catalog.VariationRepository;
import upcafe.service.MenuService;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

	@InjectMocks
	private MenuService menuService;
	
	@Mock
	private VariationRepository variationRepository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	@Test
	public void getMenu_EmptyMenu_Success() {
		when(categoryRepository.findAll()).thenReturn(new ArrayList<Category>());
		
		MenuDTO menu = menuService.getMenu();
		
		assertNotNull(menu);
		
		assertEquals(0, menu.getCategories().size());
	}
	
	@Test
	public void getMenu_Success() {
		final String BATCH_UPDATE_ID = "BATCH_UPDATE_ID_1";
		final LocalDateTime UPDATED_AT =  LocalDateTime.of(2020, 6, 20, 10, 12);
		
		Variation avocadoVariation = new Variation.Builder("AVOCADO_VARIATION").batchUpdateId(BATCH_UPDATE_ID).lastUpdated(UPDATED_AT)
				.image(null).inStock(true).name("Regular").price(2)
				.build();
		
		Item avocadoItem = new Item.Builder("ITEM_ID_1").batchUpdateId(BATCH_UPDATE_ID).lastUpdated(UPDATED_AT).variations(Arrays.asList(avocadoVariation))
				.description("").name("Avocado Toast").modifierLists(null).build();
		
		Variation grilledCheeseVariation = new Variation.Builder("GRILLED_CHEESE_VARIATION").batchUpdateId(BATCH_UPDATE_ID).lastUpdated(UPDATED_AT)
				.image(null).inStock(true).name("Regular").item(null).price(2).build();
		
		
		Modifier americanCheeseModifier = new Modifier.Builder("AMERICAN_CHEESE_1")
				.batchUpdateId(BATCH_UPDATE_ID).lastUpdated(UPDATED_AT)
				.name("American").price(.5).inStock(true)
				.onByDefault(true)
				.modifierList(new ModifierList.Builder("MODIFIER_LIST_ID_1").build())
				.build();
		
		Modifier provoloneCheeseModifier = new Modifier.Builder("PROVOLONE_CHEESE_1")
				.batchUpdateId(BATCH_UPDATE_ID).lastUpdated(UPDATED_AT)
				.name("Provolone").price(.5).inStock(true)
				.onByDefault(true)
				.modifierList(new ModifierList.Builder("MODIFIER_LIST_ID_1").build())
				.build();
		
		ModifierList cheeseModifierList = new ModifierList.Builder("MODIFIER_LIST_ID_1")
				.batchUpdateId(BATCH_UPDATE_ID).lastUpdated(UPDATED_AT)
				.image(null).selectionType("SINGLE")
				.name("Cheeses")
				.modifiers(Arrays.asList(americanCheeseModifier, provoloneCheeseModifier))
				.build();
		
		
		Item grilledCheeseItem = new Item.Builder("ITEM_ID_2")
				.batchUpdateId(BATCH_UPDATE_ID).lastUpdated(UPDATED_AT)
				.description("").name("Grilled Cheese")
				.variations(Arrays.asList(grilledCheeseVariation))
				.modifierLists(new HashSet<>(Arrays.asList(cheeseModifierList)))
				.build();
		
		Category sandwichCategory = new Category.Builder("CATEGORY_ID_1").batchUpdateId(BATCH_UPDATE_ID).lastUpdated(UPDATED_AT)
				.name("Sandwiches").items(Arrays.asList(avocadoItem, grilledCheeseItem)).build();
		
		List<Category> categories = Arrays.asList(sandwichCategory);
		
		when(categoryRepository.findAll()).thenReturn(categories);
		
		MenuDTO menu = menuService.getMenu();
		
		assertNotNull(menu);
		assertEquals(1, menu.getCategories().size());
		
		

		assertEquals(categories.get(0).getName(), menu.getCategories().get(0).getName());
		
		List<MenuItemDTO> menuItems = menu.getCategories().get(0).getItems();
		List<Item> categoryItems = categories.get(0).getItems();
		
		menuItems.forEach(menuItem -> assertNotEquals("Regular", menuItem.getName()));
		
		int numberOfVariations = 0;
		
		for(int itemIndex = 0; itemIndex < categoryItems.size(); ++itemIndex) 
			numberOfVariations += categoryItems.get(itemIndex).getVariations().size();
		
		assertEquals(numberOfVariations, menuItems.size());
		
		MenuItemDTO avocadoMenuItem = menuItems.stream()
			.filter(menuItem -> menuItem.getId().equals("AVOCADO_VARIATION"))
			.findAny()
			.orElse(null);
		
		assertEquals("AVOCADO_VARIATION", avocadoMenuItem.getId());
		assertEquals("Avocado Toast", avocadoMenuItem.getName());
		assertEquals(2, avocadoMenuItem.getPrice());
		assertEquals(true, avocadoMenuItem.getInStock());
		assertEquals("", avocadoMenuItem.getDescription());
		assertNull(avocadoMenuItem.getModifierLists());
		assertNull(avocadoMenuItem.getImage());

		
		MenuItemDTO grilledCheeseMenuItem = menuItems.stream()
				.filter(menuItem -> menuItem.getId().equals("GRILLED_CHEESE_VARIATION"))
				.findAny()
				.orElse(null);
		
		assertEquals("GRILLED_CHEESE_VARIATION", grilledCheeseMenuItem.getId());
		assertEquals("Grilled Cheese", grilledCheeseMenuItem.getName());
		assertEquals(true, grilledCheeseMenuItem.getInStock());
		assertEquals(1, grilledCheeseMenuItem.getModifierLists().size());
		
		ModifierListDTO modifierList = grilledCheeseMenuItem.getModifierLists().stream()
				.findFirst()
				.orElse(null);
		
		assertEquals(2, modifierList.getModifiers().size());
		assertEquals("Cheeses", modifierList.getName());
		assertEquals("SINGLE", modifierList.getSelectionType());
		assertNull(modifierList.getImage());
	}
	
	@Test
	public void getMenuItemById_EmptyStringPassedForId_ExceptionThrown() {
		assertThrows(NonExistentIdFoundException.class,() -> menuService.getMenuItemById(""));		
	}
	
	@Test
	public void getMenuItemById_NullPassedForId_ExceptionThrown() {
		assertThrows(NonExistentIdFoundException.class,() -> menuService.getMenuItemById(null));
	}
	
	@Test
	public void getMenuItemById_NonexistentIdPassed_ExceptionThrown() {
		final String ID = "VARIATION_ID_1";
		
		when(variationRepository.findById(ID)).thenReturn(Optional.empty());
		
		assertThrows(NonExistentIdFoundException.class, () -> menuService.getMenuItemById(ID));
	}
	
	@Test
	public void getMenuItemById_ValidIdPassed_NameFromVariation() {
		
		final String ID = "APWDMW9FIWENOWEF0JKWJEF";
		final String BATCH_UPDATE_ID = "WOOWD00Q-2-11";
		final LocalDateTime UPDATED_AT = LocalDateTime.of(2020, 6, 20, 10, 12);
		
		Image image = new Image.Builder("SQ0JJ3EF02032#r")
				.batchUpdateId(BATCH_UPDATE_ID)
				.lastUpdated(UPDATED_AT)
				.caption("A picture of a croissant")
				.name("Croissant Item Image")
				.url("http://www.google.com")
				.build();
		
		Item item = new Item.Builder("ITEM_ID_1")
				.lastUpdated(UPDATED_AT)
				.batchUpdateId(BATCH_UPDATE_ID)
				.category(new Category.Builder("CATEGORY_ID_1").build())
				.description("This is a croissant")
				.modifierLists(new HashSet<>())
				.variations(new ArrayList<>())
				.name("Croissant")
				.build();
		
		Variation variation = new Variation(ID, "Butter Croissant", 2, image, item, BATCH_UPDATE_ID, UPDATED_AT);
		variation.setInStock(true);
		
		when(variationRepository.findById(ID)).thenReturn(Optional.of(variation));
		
		MenuItemDTO menuItem = menuService.getMenuItemById(ID);
		
		assertNotNull(menuItem);
		
		assertEquals(ID, menuItem.getId());
		assertEquals(variation.getName(), menuItem.getName());
		assertEquals(item.getDescription(), menuItem.getDescription());
		assertEquals(variation.getInStock(), menuItem.getInStock());
		assertEquals(variation.getPrice(), menuItem.getPrice());
		assertEquals(item.getModifierLists().size(), menuItem.getModifierLists().size());
		assertEquals(variation.getImage().getUrl(), menuItem.getImage().getUrl());
		assertEquals(variation.getImage().getName(), menuItem.getImage().getName());
		assertEquals(variation.getImage().getCaption(), menuItem.getImage().getCaption());
	}
	
	@Test
	public void getMenuItemById_ValidIdPassed_NameFromItem() {
		
		final String ID = "APWDMW9FIWENOWEF0JKWJEF";
		final String BATCH_UPDATE_ID = "WOOWD00Q-2-11";
		final LocalDateTime UPDATED_AT = LocalDateTime.of(2020, 6, 20, 10, 12);
		
		Image image = new Image.Builder("SQ0JJ3EF02032#r")
				.batchUpdateId(BATCH_UPDATE_ID)
				.lastUpdated(UPDATED_AT)
				.caption("A picture of a croissant")
				.name("Croissant Item Image")
				.url("http://www.google.com")
				.build();
		
		Item item = new Item.Builder("ITEM_ID_1")
				.lastUpdated(UPDATED_AT)
				.batchUpdateId(BATCH_UPDATE_ID)
				.category(new Category.Builder("CATEGORY_ID_1").build())
				.description("This is a croissant")
				.modifierLists(new HashSet<>())
				.variations(new ArrayList<>())
				.name("Croissant")
				.build();
		
		Variation variation = new Variation(ID, "Regular", 2, image, item, BATCH_UPDATE_ID, UPDATED_AT);
		variation.setInStock(true);
		
		when(variationRepository.findById(ID)).thenReturn(Optional.of(variation));
		
		MenuItemDTO menuItem = menuService.getMenuItemById(ID);
		
		assertNotNull(menuItem);
		
		assertEquals(ID, menuItem.getId());
		assertEquals(item.getName(), menuItem.getName());
		assertEquals(item.getDescription(), menuItem.getDescription());
		assertEquals(variation.getInStock(), menuItem.getInStock());
		assertEquals(variation.getPrice(), menuItem.getPrice());
		assertEquals(item.getModifierLists().size(), menuItem.getModifierLists().size());
		assertEquals(variation.getImage().getUrl(), menuItem.getImage().getUrl());
		assertEquals(variation.getImage().getName(), menuItem.getImage().getName());
		assertEquals(variation.getImage().getCaption(), menuItem.getImage().getCaption());
	}
}
