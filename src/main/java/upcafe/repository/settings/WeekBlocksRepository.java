package upcafe.repository.settings;

import java.sql.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import upcafe.entity.settings.WeekBlocks;

public interface WeekBlocksRepository extends JpaRepository<WeekBlocks, Date> {

    // public List<WeekBlocks> getByWeekOf(LocalDate weekOf);

}
