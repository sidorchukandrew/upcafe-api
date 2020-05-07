package upcafe.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import upcafe.dto.catalog.CategoryDTO;
import upcafe.dto.catalog.MenuItemDTO;
import upcafe.service.CatalogService;

//
@RestController
@CrossOrigin(origins = "*")
public class CatalogController {
    
    @Autowired
    private CatalogService catalogService;

    // @GetMapping(path = "/catalog/update")
    // public String getCatalog() {
    //
    // updateService.updateLocalCatalog();
    // return "OK";
    // }
    //
    // @GetMapping(path = "/catalog/{category}")
    // public Catalog getCatalogByCategory(@PathVariable(name = "category") String
    // category) {
    //
    // return catalogService.getCatalogByCategory(category);
    // }
    //

    @GetMapping(path = "/catalog")
    public Map<String, List<MenuItemDTO>> getCatalogByCategory(@RequestParam(name = "category") String category) {
        Map<String, List<MenuItemDTO>> catalogResponse = new HashMap<String, List<MenuItemDTO>>();
        catalogResponse.put("items", catalogService.getItemsForCategory(category));

        return catalogResponse;
    }

    @GetMapping(path = "/categories")
    public Map<String, List<CategoryDTO>> getCategories() {
        Map<String, List<CategoryDTO>> categoriesResponse = new HashMap<String, List<CategoryDTO>>();
        categoriesResponse.put("categories", catalogService.getCategories());

        return categoriesResponse;

    }
    // @GetMapping(path = "/catalog/categories")
    // public Categories getCategories() {
    // Categories categories = new Categories();
    // categories.setCategories(catalogService.getCategories());
    //
    // return categories;
    // }
    //
    // @GetMapping(path = "/catalog/variations/{id}")
    // public LineItem getByVariationId(@PathVariable(name = "id") String id) {
    // return catalogService.getCategoryItemByVariationId(id);
    // }
    //
    // @PostMapping(path = "/catalog/image")
    // public void createImage(@RequestBody ImageData image) {
    //
    // if(image.getCatalogObjectId() == null)
    // throw new MissingParameterException("catalog object id");
    //
    // if(image.getCatalogObjectId() == null)
    // throw new MissingParameterException("url");
    //
    // System.out.println(image);
    // this.catalogService.createImage(image.getCatalogObjectId(), image.getUrl(),
    // image.getCaption(), image.getName());
    // this.updateService.updateLocalCatalog();
    // }
    //
}
