package upcafe.repository.settings;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import upcafe.entity.settings.WeekBlocks;
import upcafe.entity.settings.WeekBlockId;

public interface WeekBlocksRepository extends JpaRepository<WeekBlocks, WeekBlockId>{

	// public List<WeekBlocks> getByWeekOf(LocalDate weekOf);
}
