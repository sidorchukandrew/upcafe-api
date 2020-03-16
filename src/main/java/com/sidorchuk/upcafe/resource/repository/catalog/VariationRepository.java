package com.sidorchuk.upcafe.resource.repository.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sidorchuk.upcafe.resource.entity.catalog.ItemVariation;

public interface VariationRepository extends JpaRepository<ItemVariation, String> {

}
