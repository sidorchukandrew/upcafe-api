package upcafe.repository.settings;

import org.springframework.data.jpa.repository.JpaRepository;

import upcafe.entity.settings.TimeBlock;

public interface BlockRepository extends JpaRepository<TimeBlock, String> {

	public TimeBlock getByDayAndId(String day, String id);
}
