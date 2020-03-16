package com.sidorchuk.upcafe.resource.repository.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sidorchuk.upcafe.resource.entity.catalog.Item;

public interface ItemRepository extends JpaRepository<Item, String> {

}
