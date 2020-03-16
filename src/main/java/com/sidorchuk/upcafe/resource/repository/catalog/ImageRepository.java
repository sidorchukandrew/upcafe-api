package com.sidorchuk.upcafe.resource.repository.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sidorchuk.upcafe.resource.entity.catalog.Image;

public interface ImageRepository extends JpaRepository<Image, String>{

}
