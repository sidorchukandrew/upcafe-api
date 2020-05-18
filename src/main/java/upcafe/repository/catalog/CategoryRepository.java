package upcafe.repository.catalog;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import upcafe.entity.catalog.Category;

public interface CategoryRepository extends JpaRepository<Category, String>{

	@Transactional
	@Modifying
	@Query("DELETE FROM Category c WHERE c.batchUpdateId != :id")
	public void deleteOldBatchUpdateIds(@Param("id") String id);
	
}
