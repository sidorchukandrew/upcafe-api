package com.sidorchuk.upcafe.resource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sidorchuk.upcafe.resource.service.CatalogService;

@RestController
public class CatalogController {

	@Autowired
	private CatalogService catalogService;
	
	@GetMapping(path = "/catalog")
	public String getCatalog() {
		
		catalogService.updateLocalCatalog();
		return "OK";
	}
	
	@GetMapping(path = "/test")
	public String test() {
		return "OK";
	}
	
}
