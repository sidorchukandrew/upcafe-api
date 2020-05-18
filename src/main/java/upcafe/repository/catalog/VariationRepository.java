package upcafe.repository.catalog;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import upcafe.entity.catalog.Variation;

public interface VariationRepository extends JpaRepository<Variation, String> {

    public List<Variation> getVariationsByItemId(String id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Variation v WHERE v.batchUpdateId != :id")
    public void deleteOldBatchUpdateIds(@Param("id") String id);
}
