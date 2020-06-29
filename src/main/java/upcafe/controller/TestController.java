package upcafe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import upcafe.service.CatalogService;
import upcafe.service.UpdateService;

@RestController
public class TestController {

    @Autowired
    private UpdateService updater;

    @Autowired
    CatalogService catalogService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/update")
    public String updateCatalog() {
        updater.updateLocalCatalog();
        return "Ok";
    }
    
}
