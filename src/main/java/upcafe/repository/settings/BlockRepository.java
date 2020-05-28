package upcafe.repository.settings;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import upcafe.entity.settings.TimeBlock;

import javax.transaction.Transactional;

public interface BlockRepository extends CrudRepository<TimeBlock, String> {

	// public TimeBlock getByDayAndId(String day, String id);

	@Query("SELECT b FROM TimeBlock b WHERE b.day = :day AND b.weekOf.weekOf = :weekOf")
	public List<TimeBlock> getTimeBlocksForDay(@Param("day") String day, @Param("weekOf") LocalDate weekOf);

	@Query("UPDATE TimeBlock b SET b.day = :day, b.open = :open, b.close = :close WHERE b.id = :id")
	@Modifying
	@Transactional
	public void updateBlock(@Param("day") String day, @Param("open") LocalTime open, @Param("close") LocalTime close, @Param("id") String id);

}
