package upcafe.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upcafe.dto.settings.TimeBlockDTO;
import upcafe.entity.settings.TimeBlock;
import upcafe.entity.settings.WeekBlocks;
import upcafe.repository.settings.BlockRepository;
import upcafe.repository.settings.WeekBlocksRepository;

@Service
public class CafeHoursService {
	
	
	@Autowired private WeekBlocksRepository weekBlockRepository;
	
	@Autowired private BlockRepository blockRepository;
	
	public TimeBlock saveNewBlock(TimeBlockDTO timeBlockDTO) {
		
		

		TimeBlock timeBlock = new TimeBlock.Builder(UUID.randomUUID().toString())
								.day(timeBlockDTO.getDay())
								.open(timeBlockDTO.getOpen())
								.close(timeBlockDTO.getClose())
								.build();

		return blockRepository.save(timeBlock);
	}
	
	public List<TimeBlock> getBlocksForWeek(String weekOf) {
		List<WeekBlock> weekBlocks = weekBlockRepository.getByWeekOf(weekOf);
		
		List<TimeBlock> timeBlocks = new ArrayList<TimeBlock>();
		
		weekBlocks.forEach(weekBlock -> {
			
			Optional<TimeBlock> block = blockRepository.findById(weekBlock.getBlockId());
			
			if(block.isPresent())
				timeBlocks.add(block.get());
		});
		
		return timeBlocks;
	}
	
	public boolean deleteBlock(String blockId, String weekOf) {
		
		WeekBlock wb = new WeekBlock(weekOf, blockId);
		weekBlockRepository.delete(wb);
		
		blockRepository.deleteById(blockId);
		return true;
	}
	
	public TimeBlock updateBlock(upcafe.model.settings.WeekBlock weekBlock) {
		return blockRepository.save(weekBlock.getBlock());
	}
	
	public List<TimeBlock> getTimeBlocksForDay(String date) {
		
		// TODO: Check if its in the correct format
		
		String dayName = getDayName(date);
		String previousMonday = getMondayOfWeek(date);
		System.out.println(previousMonday);
		
		List<WeekBlock> blocksForWeek = weekBlockRepository.getByWeekOf(previousMonday);
		List<TimeBlock> blocksForTheDay = new ArrayList<TimeBlock>();
		blocksForWeek.forEach(weekBlock -> {
			TimeBlock timeBlock = blockRepository.getByDayAndId(dayName, weekBlock.getBlockId());
			if(timeBlock != null)
				blocksForTheDay.add(timeBlock);
		});

		return blocksForTheDay;
	}
	
	private String getMondayOfWeek(String dateRequest) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy");
		
		LocalDate internalDateRequest = LocalDate.parse(dateRequest, formatter);
		
		LocalDate now = LocalDate.now();
		if(now.get(ChronoField.DAY_OF_WEEK) == 1) {
			return now.format(formatter);
		}
	
		LocalDate previousMonday = internalDateRequest.with( TemporalAdjusters.previous( DayOfWeek.MONDAY ) );
		return previousMonday.format(formatter);
	}
	
	private String getDayName(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy");
		DateTimeFormatter justDayFormatter = DateTimeFormatter.ofPattern("EEEE");
		LocalDate internalDateRequest = LocalDate.parse(date, formatter);
		
		return internalDateRequest.format(justDayFormatter);
	}
}
