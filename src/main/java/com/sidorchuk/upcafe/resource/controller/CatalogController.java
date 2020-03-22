package com.sidorchuk.upcafe.resource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.sidorchuk.upcafe.resource.model.catalog.Catalog;
import com.sidorchuk.upcafe.resource.model.catalog.Categories;
import com.sidorchuk.upcafe.resource.model.catalog.LineItem;
import com.sidorchuk.upcafe.resource.service.CatalogService;

@RestController
@CrossOrigin(origins = "*")
public class CatalogController {

	@Autowired
	private CatalogService catalogService;
	
	@GetMapping(path = "/catalog/update")
	public String getCatalog() {
		
		catalogService.updateLocalCatalog();
		return "OK";
	}
	
	@GetMapping(path = "/catalog/{category}")
	public Catalog getCatalogByCategory(@PathVariable(name = "category") String category) {
		return catalogService.getCatalogByCategory(category);
	}
	
	@GetMapping(path = "/catalog/categories")
	public Categories getCategories() {
		Categories categories = new Categories();
		categories.setCategories(catalogService.getCategories());
		
		return categories;
	}
	
	@GetMapping(path = "/catalog/variations/{id}")
	public LineItem getByVariationId(@PathVariable(name = "id") String id) {
		return catalogService.getCategoryItemByVariationId(id);
	}
	
}
