package upcafe.repository.settings;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import upcafe.entity.settings.TimeBlock;

public interface BlockRepository extends CrudRepository<TimeBlock, String> {

	// public TimeBlock getByDayAndId(String day, String id);

	@Query("SELECT b FROM TimeBlock b WHERE b.day = :day")
	public List<TimeBlock> getTimeBlocksForDay(@Param("day") LocalDate day);

}
