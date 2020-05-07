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
import upcafe.dto.settings.WeekBlocksDTO;
import upcafe.entity.settings.TimeBlock;
import upcafe.entity.settings.WeekBlocks;
import upcafe.error.MissingParameterException;
import upcafe.repository.settings.BlockRepository;
import upcafe.repository.settings.WeekBlocksRepository;
import upcafe.utils.TimeUtils;

@Service
public class CafeHoursService {

	@Autowired
	private WeekBlocksRepository weekBlockRepository;

	@Autowired
	private BlockRepository blockRepository;

	public TimeBlockDTO saveNewBlock(TimeBlockDTO timeBlockDTO) {

		validateTimeBlockSaveRequest(timeBlockDTO);

		TimeBlock timeBlock = new TimeBlock.Builder(UUID.randomUUID().toString()).day(timeBlockDTO.getDay())
				.open(timeBlockDTO.getOpen()).close(timeBlockDTO.getClose())
				.weekOf(new WeekBlocks.Builder(TimeUtils.getMondayOfWeek(timeBlockDTO.getDay())).build()).build();

		blockRepository.save(timeBlock);
		return timeBlockDTO;
	}

	private boolean validateTimeBlockSaveRequest(TimeBlockDTO timeBlock) {

		if (timeBlock.getClose() == null)
			throw new MissingParameterException("close");

		if (timeBlock.getOpen() == null)
			throw new MissingParameterException("open");

		if (timeBlock.getDay() == null)
			throw new MissingParameterException("day");

		return true;
	}

	public WeekBlocksDTO getBlocksForWeek(LocalDate dayInWeek) {
		
		LocalDate weekOf = TimeUtils.getMondayOfWeek(dayInWeek);
		
		Optional<WeekBlocks> blocksForWeekOpt = weekBlockRepository.findById(weekOf);

		if(blocksForWeekOpt.isPresent()) {

			List<TimeBlockDTO> timeBlockDTOs = new ArrayList<TimeBlockDTO>();

			blocksForWeekOpt.get().getTimeBlocks().forEach(block -> {
				timeBlockDTOs.add(new TimeBlockDTO.Builder(block.getId())
									.day(block.getDay())
									.open(block.getOpen())
									.close(block.getClose())
									.build()
				);
			});
			WeekBlocksDTO blocksForWeekDTO = new WeekBlocksDTO.Builder(blocksForWeekOpt.get().getWeekOf())
												.blocks(timeBlockDTOs)
												.build();	
												
			return blocksForWeekDTO;
		}

		return null;
	}
	
	// public boolean deleteBlock(String blockId, String weekOf) {
		
	// 	WeekBlock wb = new WeekBlock(weekOf, blockId);
	// 	weekBlockRepository.delete(wb);
		
	// 	blockRepository.deleteById(blockId);
	// 	return true;
	// }
	
	// public TimeBlock updateBlock(upcafe.model.settings.WeekBlock weekBlock) {
	// 	return blockRepository.save(weekBlock.getBlock());
	// }
	
	// public List<TimeBlock> getTimeBlocksForDay(String date) {
		
	// 	// TODO: Check if its in the correct format
		
	// 	String dayName = getDayName(date);
	// 	String previousMonday = getMondayOfWeek(date);
	// 	System.out.println(previousMonday);
		
	// 	List<WeekBlock> blocksForWeek = weekBlockRepository.getByWeekOf(previousMonday);
	// 	List<TimeBlock> blocksForTheDay = new ArrayList<TimeBlock>();
	// 	blocksForWeek.forEach(weekBlock -> {
	// 		TimeBlock timeBlock = blockRepository.getByDayAndId(dayName, weekBlock.getBlockId());
	// 		if(timeBlock != null)
	// 			blocksForTheDay.add(timeBlock);
	// 	});

	// 	return blocksForTheDay;
	// }
	
	// private String getMondayOfWeek(String dateRequest) {
	// 	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy");
		
	// 	LocalDate internalDateRequest = LocalDate.parse(dateRequest, formatter);
		
	// 	LocalDate now = LocalDate.now();
	// 	if(now.get(ChronoField.DAY_OF_WEEK) == 1) {
	// 		return now.format(formatter);
	// 	}
	
	// 	LocalDate previousMonday = internalDateRequest.with( TemporalAdjusters.previous( DayOfWeek.MONDAY ) );
	// 	return previousMonday.format(formatter);
	// }
	
	// private String getDayName(String date) {
	// 	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy");
	// 	DateTimeFormatter justDayFormatter = DateTimeFormatter.ofPattern("EEEE");
	// 	LocalDate internalDateRequest = LocalDate.parse(date, formatter);
		
	// 	return internalDateRequest.format(justDayFormatter);
	// }
}
