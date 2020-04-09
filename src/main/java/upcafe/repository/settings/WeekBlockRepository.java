package upcafe.repository.settings;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import upcafe.entity.settings.WeekBlock;
import upcafe.entity.settings.WeekBlockId;

public interface WeekBlockRepository extends JpaRepository<WeekBlock, WeekBlockId>{

	public List<WeekBlock> getByWeekOf(String weekOf);
}
