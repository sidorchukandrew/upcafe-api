package upcafe.repository.settings;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import upcafe.entity.settings.TimeBlock;
import upcafe.entity.settings.WeekBlocks;

public interface BlockRepository extends CrudRepository<TimeBlock, String> {


    @Query("SELECT b FROM TimeBlock b WHERE b.day = :day AND b.weekOf.weekOf = :weekOf")
    public List<TimeBlock> getTimeBlocksForDay(@Param("day") String day, @Param("weekOf") LocalDate weekOf);

    @Query("UPDATE TimeBlock b SET b.day = :day, b.open = :open, b.close = :close WHERE b.id = :id")
    @Modifying
    @Transactional
    public void updateBlock(@Param("day") String day, @Param("open") LocalTime open, @Param("close") LocalTime close, @Param("id") String id);

    @Query(nativeQuery = true, value = "SELECT * FROM time_block WHERE time_block.week_of = :weekOf")
    List<TimeBlock> getBlocksByWeekOf(@Param("weekOf") String weekOf);
    
    @Query(nativeQuery = true, value = "INSERT INTO time_block (id, open, close, week_of, day) VALUES (:id, :open, :close, :weekOf, :day)")
    @Modifying
    @Transactional
    void saveBlock(@Param("id") String id, @Param("open") LocalTime open, @Param("close") LocalTime close, @Param("weekOf") String weekOf, @Param("day") String day);
}
