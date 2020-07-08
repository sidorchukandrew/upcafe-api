package upcafe.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.squareup.square.models.CatalogObject;

import upcafe.dto.catalog.CatalogDTO;
import upcafe.dto.catalog.CatalogInventoryUpdate;
import upcafe.dto.catalog.ImageDTO;
import upcafe.entity.catalog.Image;
import upcafe.service.CatalogService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/catalog")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @GetMapping
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    public Map<String, CatalogDTO> getCatalog() {
        System.out.println(catalogService.getCatalog());

        Map<String, CatalogDTO> catalogResponse = new HashMap<String, CatalogDTO>();
        catalogResponse.put("catalog", catalogService.getCatalog());
        return catalogResponse;
    }

    @PutMapping(path = "/inventory")
    @PreAuthorize(value = "hasAnyRole('ADMIN', 'STAFF')")
    public Map<String, Boolean> updateInventory(@RequestBody CatalogInventoryUpdate updatedInventory) {
        Map<String, Boolean> updateInventoryResponse = new HashMap<>();

        if (catalogService.updateInventory(updatedInventory))
            updateInventoryResponse.put("success", true);

        else
            updateInventoryResponse.put("success", false);

        return updateInventoryResponse;
    }

    @PostMapping(path = "/images")
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
    
}
