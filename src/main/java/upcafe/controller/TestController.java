package upcafe.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import upcafe.entity.catalog.Category;
import upcafe.entity.catalog.Item;
import upcafe.repository.catalog.CategoryRepository;
import upcafe.repository.catalog.ItemRepository;

@RestController
public class TestController {

	@Autowired
	private ItemRepository itemRepo;
	@Autowired
	private CategoryRepository categoryRepo;

	@GetMapping("/categories")
	public Iterable<Category> getAllCatgories() {
		return categoryRepo.findAll();
	}

	@GetMapping("/items")
	public Iterable<Item> getAllItems() {
		return itemRepo.findAll();
	}

	@GetMapping("/categories/demo")
	public Category getDemoCategory() {
		final Category category = new Category();
		category.setBatchUpdateId("AH92HRHI2NFKENF02");
		category.setId("a991991heh821");
		category.setName("SANDWICH");
		category.setUpdatedAt(LocalDateTime.now());

		return category;
	}

	@GetMapping("/items/demo")
	public Item getDemoItem() {

		final Category category = new Category();
		category.setBatchUpdateId("AH92HRHI2NFKENF02");
		category.setId("a991991heh821");
		category.setName("SANDWICH");
		category.setUpdatedAt(LocalDateTime.now());

		final Item item = new Item();
		item.setBatchUpdateId("AH92HRHI2NFKENF02");
		item.setCategory(category);
		item.setDescription("This is a delicious snack.");
		item.setUpdatedAt(LocalDateTime.now());
		item.setName("Turkey Sandwich");
		return item;
	}

	@PostMapping(path = "/categories")
	public Category saveCategory(@RequestBody final Category category) {
		return categoryRepo.save(category);
	}

	@PostMapping(path = "/items")
	public Item saveItem(@RequestBody final Item item) {
		System.out.println(item);
		return itemRepo.save(item);
	}

}
