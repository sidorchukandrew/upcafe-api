package upcafe.repository.settings;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import upcafe.entity.settings.WeekBlocks;

public interface WeekBlocksRepository extends JpaRepository<WeekBlocks, LocalDate> {

    // public List<WeekBlocks> getByWeekOf(LocalDate weekOf);

}
