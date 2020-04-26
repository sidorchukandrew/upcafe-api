//package upcafe.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import upcafe.error.MissingParameterException;
//import upcafe.model.catalog.Catalog;
//import upcafe.model.catalog.Categories;
//import upcafe.model.catalog.ImageData;
//import upcafe.model.catalog.LineItem;
//import upcafe.service.CatalogService;
//import upcafe.service.UpdateService;
//
//@RestController
//@CrossOrigin(origins = "*")
//public class CatalogController {
//
//	@Autowired
//	private CatalogService catalogService;
//	
//	@Autowired
//	private UpdateService updateService;
//	
//	@GetMapping(path = "/catalog/update")
//	public String getCatalog() {
//		
//		updateService.updateLocalCatalog();
//		return "OK";
//	}
//	
//	@GetMapping(path = "/catalog/{category}")
//	public Catalog getCatalogByCategory(@PathVariable(name = "category") String category) {
//
//		return catalogService.getCatalogByCategory(category);
//	}
//	
//	@GetMapping(path = "/catalog/categories")
//	public Categories getCategories() {
//		Categories categories = new Categories();
//		categories.setCategories(catalogService.getCategories());
//		
//		return categories;
//	}
//	
//	@GetMapping(path = "/catalog/variations/{id}")
//	public LineItem getByVariationId(@PathVariable(name = "id") String id) {
//		return catalogService.getCategoryItemByVariationId(id);
//	}
//	
//	@PostMapping(path = "/catalog/image")
//	public void createImage(@RequestBody ImageData image) {
//		
//		if(image.getCatalogObjectId() == null)
//			throw new MissingParameterException("catalog object id");
//		
//		if(image.getCatalogObjectId() == null)
//			throw new MissingParameterException("url");		
//		
//		System.out.println(image);
//		this.catalogService.createImage(image.getCatalogObjectId(), image.getUrl(), image.getCaption(), image.getName());
//		this.updateService.updateLocalCatalog();
//	}
//	
//}
