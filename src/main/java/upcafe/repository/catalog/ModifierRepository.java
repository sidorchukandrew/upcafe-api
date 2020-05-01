package upcafe.repository.catalog;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import upcafe.dto.catalog.ModifierDTO;
import upcafe.entity.catalog.Modifier;

public interface ModifierRepository extends JpaRepository<Modifier, String> {

    public List<Modifier> getModifiersByModListId(String id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Modifier m WHERE m.batchUpdateId != :id")
    public void deleteOldBatchUpdateIds(@Param("id") String id);

    @Query("SELECT new upcafe.dto.catalog.ModifierDTO(m.id, m.price, m.name, m.onByDefault, m.inStock, m.image) FROM Modifier m")
    public Iterable<ModifierDTO> getModifiers();
}
