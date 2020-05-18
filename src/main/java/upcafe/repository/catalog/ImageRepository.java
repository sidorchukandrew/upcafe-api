package upcafe.repository.catalog;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import upcafe.entity.catalog.Image;
import java.util.List;
import upcafe.dto.catalog.ImageDTO;

public interface ImageRepository extends JpaRepository<Image, String> {

	@Transactional
	@Modifying
	@Query("DELETE FROM Image i WHERE i.batchUpdateId != :id")
	public void deleteOldBatchUpdateIds(@Param("id") String id);

	@Query("SELECT new upcafe.dto.catalog.ImageDTO(i.name, i.url, i.caption) FROM Image i")
	public List<ImageDTO> getAllImages();
}
