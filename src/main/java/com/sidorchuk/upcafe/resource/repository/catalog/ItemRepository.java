package com.sidorchuk.upcafe.resource.repository.catalog;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sidorchuk.upcafe.resource.entity.catalog.Item;

public interface ItemRepository extends JpaRepository<Item, String> {

	public List<Item> getItemsByCategoryName(String name);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM Item i WHERE i.batchUpdateId != :id")
	public void deleteOldBatchUpdateIds(@Param("id") String id);
}
