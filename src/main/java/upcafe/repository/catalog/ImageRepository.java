package upcafe.repository.catalog;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import upcafe.entity.catalog.Image;

public interface ImageRepository extends JpaRepository<Image, String> {

	@Transactional
	@Modifying
	@Query("DELETE FROM Image i WHERE i.batchUpdateId != :id")
	public void deleteOldBatchUpdateIds(@Param("id") String id);
}
