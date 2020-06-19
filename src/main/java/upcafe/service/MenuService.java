package upcafe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upcafe.dto.menu.CategoryDTO;
import upcafe.dto.menu.MenuDTO;
import upcafe.dto.menu.MenuItemDTO;
import upcafe.entity.catalog.Category;
import upcafe.entity.catalog.Variation;
import upcafe.error.NonExistentIdFoundException;
import upcafe.repository.catalog.CategoryRepository;
import upcafe.repository.catalog.VariationRepository;
import upcafe.utils.TransferUtils;

@Service
public class MenuService {

	@Autowired
	private VariationRepository variationRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	public MenuDTO getMenu() {
		List<Category> categoriesDB = categoryRepository.findAll();
		List<CategoryDTO> categoryDTOs = new ArrayList<CategoryDTO>();

		categoriesDB.forEach(category -> {
			List<MenuItemDTO> menuItems = new ArrayList<MenuItemDTO>();

			category.getItems().forEach(itemDB -> {

				itemDB.getVariations().forEach(variationDB -> {
					String nameOfMenuItem = (variationDB.getName().compareTo("Regular") == 0)
							? itemDB.getName()
							: variationDB.getName();
							
					menuItems.add(new MenuItemDTO.Builder(variationDB.getId())
							.description(itemDB.getDescription())
							.image(TransferUtils.toImageDTO(variationDB.getImage()))
							.modifierLists(TransferUtils.toModifierListDTOs(itemDB.getModifierLists()))
							.price(variationDB.getPrice()).inStock(variationDB.getInStock()).name(nameOfMenuItem)
							.build());
				});
			});

			categoryDTOs.add(new CategoryDTO.Builder(category.getName()).items(menuItems).build());
		});

		return new MenuDTO.Builder().categories(categoryDTOs).build();
	}

	public MenuItemDTO getMenuItemById(String id) {
		
		if(id == null || id.compareTo("") == 0)
			throw new NonExistentIdFoundException("null", "Variation");
		
		Optional<Variation> variationOpt = variationRepository.findById(id);

		if (variationOpt.isPresent()) {

			Variation variation = variationOpt.get();
			String nameOfMenuItem = (variation.getName().compareTo("Regular") == 0) ? variation.getItem().getName()
					: variation.getName();

			return new MenuItemDTO.Builder(variation.getId()).description(variation.getItem().getDescription())
					.image(TransferUtils.toImageDTO(variation.getImage()))
					.modifierLists(TransferUtils.toModifierListDTOs(variation.getItem().getModifierLists()))
					.price(variation.getPrice()).inStock(variation.getInStock()).name(nameOfMenuItem).build();
		}
		
		else {
			throw new NonExistentIdFoundException(id, "Variation");
		}
	}

}
