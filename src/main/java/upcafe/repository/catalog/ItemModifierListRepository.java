package upcafe.repository.catalog;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import upcafe.entity.catalog.ItemModifierList;

public interface ItemModifierListRepository extends JpaRepository<ItemModifierList, String> {
	
	public List<ItemModifierList> getItemModifierListsByItemId(String itemId);
	
	@Transactional
	@Modifying
	@Query("DELETE FROM ItemModifierList i WHERE i.batchUpdateId != :id")
	public void deleteOldBatchUpdateIds(@Param("id") String id);
}
