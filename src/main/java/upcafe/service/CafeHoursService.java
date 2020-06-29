package upcafe.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
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

	public TimeBlockDTO saveNewBlock(TimeBlockDTO timeBlockDTO, String date) {

		validateTimeBlockSaveRequest(timeBlockDTO);

		LocalDate weekOf = TimeUtils.toLocalDate(date);
		System.out.println("Converting weekOf to LocalDate : " + weekOf);

		TimeBlock timeBlock = new TimeBlock.Builder(UUID.randomUUID().toString()).day(timeBlockDTO.getDay())
				.open(timeBlockDTO.getOpen()).close(timeBlockDTO.getClose())
				.weekOf(new WeekBlocks.Builder(TimeUtils.getMondayOfWeek(weekOf)).build()).build();

		if(weekBlockRepository.getWeekBlockById(weekOf.toString()).size() <= 0) {
			System.out.println("This week block doesn't exist yet");
			weekBlockRepository.saveWeekBlock(weekOf.toString());
		}
		
		blockRepository.saveBlock(timeBlock.getId(), timeBlock.getOpen(), timeBlock.getClose(), timeBlock.getWeekOf().getWeekOf().toString(), timeBlock.getDay());

		timeBlockDTO.setId(timeBlock.getId());
		System.out.println("Returning the saved TimeBlock : " + timeBlockDTO);
		return timeBlockDTO;
	}

	public boolean validateTimeBlockSaveRequest(TimeBlockDTO timeBlock) {

		if (timeBlock.getClose() == null)
			throw new MissingParameterException("close");

		if (timeBlock.getOpen() == null)
			throw new MissingParameterException("open");

		if (timeBlock.getDay() == null)
			throw new MissingParameterException("day");

		return true;
	}

	private boolean validateUpdateBlockRequest(TimeBlockDTO block) {

		if (block.getDay() == null)
			throw new MissingParameterException("day");

		if (block.getClose() == null)
			throw new MissingParameterException("close");

		if (block.getOpen() == null)
			throw new MissingParameterException("open");

		if (block.getId() == null)
			throw new MissingParameterException("id");

		return true;
	}

	public WeekBlocksDTO getBlocksForWeek(LocalDate dayInWeek) {

		LocalDate weekOf = TimeUtils.getMondayOfWeek(dayInWeek);
		System.out.println("Converting weekOf to LocalDate : " + weekOf);

		List<TimeBlockDTO> timeBlockDTOs = new ArrayList<TimeBlockDTO>();
		System.out.println("Retrieving time blocks with the weekOf : " + weekOf);
		List<TimeBlock> timeBlocks = blockRepository.getBlocksByWeekOf(weekOf.toString());
		
		System.out.println("Retrieved " + timeBlocks.size() + " blocks");
		
		timeBlocks.forEach(block -> {
			timeBlockDTOs.add(new TimeBlockDTO.Builder(block.getId()).day(block.getDay()).open(block.getOpen())
					.close(block.getClose()).build());
		});

		WeekBlocksDTO blocksForWeekDTO = new WeekBlocksDTO.Builder(weekOf)
				.blocks(timeBlockDTOs).build();

		System.out.println("HERE'S THE BLOCKS : " + blocksForWeekDTO + "\n");

		return blocksForWeekDTO;
	}

	public boolean deleteBlock(String blockId) {
		blockRepository.deleteById(blockId);
		return true;
	}

	public TimeBlockDTO updateBlock(TimeBlockDTO blockToUpdate) {

		if (validateUpdateBlockRequest(blockToUpdate)) {
			blockRepository.updateBlock(blockToUpdate.getDay(), blockToUpdate.getOpen(), blockToUpdate.getClose(),
					blockToUpdate.getId());
			return blockToUpdate;
		}

		return null;
	}

	public List<TimeBlockDTO> getTimeBlocksForDay(LocalDate date) {

		List<TimeBlockDTO> blocksDTO = new ArrayList<TimeBlockDTO>();

		LocalDate weekOf = TimeUtils.getMondayOfWeek(date);
		String dayOfWeek = date.getDayOfWeek().toString();

		System.out.println("Checking the time block repository for a TimeBlock whose weekOf : " + weekOf
				+ " and dayOfWeek : " + dayOfWeek);
		List<TimeBlock> blocks = blockRepository.getTimeBlocksForDay(dayOfWeek, weekOf);

		blocks.forEach(blockDB -> {
			blocksDTO.add(new TimeBlockDTO.Builder(blockDB.getId()).open(blockDB.getOpen()).close(blockDB.getClose())
					.day(blockDB.getDay()).build());
		});

		System.out.println("Here are the blocks : " + blocksDTO);
		return blocksDTO;
	}
}
