package upcafe.repository.settings;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import upcafe.entity.settings.WeekBlocks;

public interface WeekBlocksRepository extends JpaRepository<WeekBlocks, LocalDate> {

	@Query(nativeQuery = true, value = "SELECT * FROM week_blocks WHERE week_blocks.week_of = :weekOf")
	List<WeekBlocks> getWeekBlockById(@Param("weekOf") String weekOf);
	
	@Modifying
	@Transactional
	@Query(nativeQuery = true, value = "INSERT INTO week_blocks (week_of) VALUES (:weekOf)")
	void saveWeekBlock(@Param("weekOf") String weekOf);
}
