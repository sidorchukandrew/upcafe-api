package upcafe.service;

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

	public TimeBlockDTO saveNewBlock(TimeBlockDTO timeBlockDTO) {

		validateTimeBlockSaveRequest(timeBlockDTO);

		TimeBlock timeBlock = new TimeBlock.Builder(UUID.randomUUID().toString()).day(timeBlockDTO.getDay())
				.open(timeBlockDTO.getOpen()).close(timeBlockDTO.getClose())
				.weekOf(new WeekBlocks.Builder(TimeUtils.getMondayOfWeek(timeBlockDTO.getDay())).build()).build();

		blockRepository.save(timeBlock);

		timeBlockDTO.setId(timeBlock.getId());
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
		System.out.println(weekOf);
		
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
	
	public boolean deleteBlock(String blockId) {
		
		blockRepository.deleteById(blockId);
		return true;
	}
	
	public TimeBlockDTO updateBlock(TimeBlockDTO blockToUpdate) {

		if(validateUpdateBlockRequest(blockToUpdate)) {
			blockRepository.save(new TimeBlock.Builder(blockToUpdate.getId())
								.day(blockToUpdate.getDay())
								.open(blockToUpdate.getOpen())
								.close(blockToUpdate.getClose())
								.weekOf(new WeekBlocks
											.Builder(TimeUtils.getMondayOfWeek(blockToUpdate.getDay()))
											.build())
								.build());

			return blockToUpdate;
		}

		return null;
	}
	
	public List<TimeBlockDTO> getTimeBlocksForDay(LocalDate day) {

		List<TimeBlockDTO> blocksDTO = new ArrayList<TimeBlockDTO>();
		
		List<TimeBlock> blocks = blockRepository.getTimeBlocksForDay(day);

		blocks.forEach(blockDB -> {
			blocksDTO.add(new TimeBlockDTO.Builder(blockDB.getId())
							.open(blockDB.getOpen())
							.close(blockDB.getClose())
							.day(blockDB.getDay())
							.build());
		});

		return blocksDTO;
	}
}
