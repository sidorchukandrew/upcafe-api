package upcafe.repository.catalog;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import upcafe.entity.catalog.Category;
import upcafe.model.catalog.CategoryView;

public interface CategoryRepository extends JpaRepository<Category, String>{

	@Transactional
	@Modifying
	@Query("DELETE FROM Category c WHERE c.batchUpdateId != :id")
	public void deleteOldBatchUpdateIds(@Param("id") String id);
	
	@Query("SELECT new upcafe.model.catalog.CategoryView(c.name) FROM Category c")
	public List<CategoryView> getCategories();
}
