package com.sidorchuk.upcafe.resource.repository.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sidorchuk.upcafe.resource.entity.catalog.Category;

public interface CategoryRepository extends JpaRepository<Category, String>{

}
