//package upcafe.repository.catalog;
//
//import java.util.List;
//
//import javax.transaction.Transactional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import upcafe.entity.catalog.ItemVariation;
//
//public interface VariationRepository extends JpaRepository<ItemVariation, String> {
//
//	public List<ItemVariation> getVariationsByItemItemId(String id);
//	
//	@Transactional
//	@Modifying
//	@Query("DELETE FROM ItemVariation i WHERE i.batchUpdateId != :id")
//	public void deleteOldBatchUpdateIds(@Param("id") String id);
//}
