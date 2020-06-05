package upcafe.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import upcafe.dto.catalog.MenuItemDTO;
import upcafe.service.MenuService;

@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping(path = "/menu/items/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'CUSTOMER')")
    public MenuItemDTO getMenuItemById(@PathVariable(name = "id") String id) {
        return menuService.getMenuItemById(id);
    }
}
