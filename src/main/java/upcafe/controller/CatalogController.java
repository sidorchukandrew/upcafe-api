package upcafe.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.squareup.square.models.CatalogObject;

import upcafe.dto.catalog.CatalogDTO;
import upcafe.dto.catalog.CatalogInventoryUpdate;
import upcafe.dto.catalog.CategoryDTO;
import upcafe.dto.catalog.ImageDTO;
import upcafe.dto.menu.MenuItemDTO;
import upcafe.entity.catalog.Image;
import upcafe.service.CatalogService;

@RestController
@CrossOrigin(origins = "*")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @GetMapping(path = "/catalog")
    @PreAuthorize(value = "hasAnyRole('CUSTOMER', 'STAFF', 'ADMIN')")
    public Map<String, List<MenuItemDTO>> getCatalogByCategory(@RequestParam(name = "category") String category) {
        Map<String, List<MenuItemDTO>> catalogResponse = new HashMap<String, List<MenuItemDTO>>();
        catalogResponse.put("items", catalogService.getItemsForCategory(category));

        return catalogResponse;
    }

    @GetMapping(path = "/categories")
    @PreAuthorize(value = "hasAnyRole('CUSTOMER', 'STAFF', 'ADMIN')")
    public Map<String, List<CategoryDTO>> getCategories() {
        Map<String, List<CategoryDTO>> categoriesResponse = new HashMap<String, List<CategoryDTO>>();
        categoriesResponse.put("categories", catalogService.getCategories());

        return categoriesResponse;

    }

    @GetMapping(path = "/catalogs")
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    public Map<String, CatalogDTO> getCatalog() {
        System.out.println(catalogService.getCatalog());

        Map<String, CatalogDTO> catalogResponse = new HashMap<String, CatalogDTO>();
        catalogResponse.put("catalog", catalogService.getCatalog());
        return catalogResponse;
    }

    @PutMapping(path = "/catalog/inventory")
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'STAFF')")
    public Map<String, Boolean> updateInventory(@RequestBody CatalogInventoryUpdate updatedInventory) {
        Map<String, Boolean> updateInventoryResponse = new HashMap<>();

        if (catalogService.updateInventory(updatedInventory))
            updateInventoryResponse.put("success", true);

        else
            updateInventoryResponse.put("success", false);

        return updateInventoryResponse;
    }

    @PostMapping(path = "/catalog/create-image")
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    public ImageDTO createImage(@RequestParam("file") MultipartFile image, @RequestParam("objectId") String objectId) {
    	
    	CatalogObject squareImage = catalogService.createImage(image, objectId);
    	Image localImage = catalogService.saveImageLocally(squareImage);
    	
    	CatalogObject catalogObjectImageAssignedTo = catalogService.getSquareCatalogObjectById(objectId);
    	catalogService.assignImageToObjectLocally(catalogObjectImageAssignedTo);
    	
    	return new ImageDTO.Builder()
    			.caption(localImage.getCaption())
    			.name(localImage.getName())
    			.url(localImage.getUrl())
    			.build();
    }	
    //
    // @GetMapping(path = "/catalog/variations/{id}")
    // public LineItem getByVariationId(@PathVariable(name = "id") String id) {
    // return catalogService.getCategoryItemByVariationId(id);
    // }
    //
    
}
