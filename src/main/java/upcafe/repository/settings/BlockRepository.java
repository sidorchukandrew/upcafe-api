package upcafe.repository.settings;

import org.springframework.data.repository.CrudRepository;

import upcafe.entity.settings.TimeBlock;

public interface BlockRepository extends CrudRepository<TimeBlock, String> {

	// public TimeBlock getByDayAndId(String day, String id);
}
