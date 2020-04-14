package upcafe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upcafe.entity.settings.TimeBlock;
import upcafe.entity.settings.WeekBlock;
import upcafe.repository.settings.BlockRepository;
import upcafe.repository.settings.WeekBlockRepository;

@Service
public class CafeHoursService {
	
	
	@Autowired private WeekBlockRepository weekBlockRepository;
	
	@Autowired private BlockRepository blockRepository;
	
	public TimeBlock saveNewBlock(upcafe.model.settings.WeekBlock weekBlock) {
		
		String id = UUID.randomUUID().toString();
		weekBlock.getBlock().setId(id);
		
		weekBlockRepository.save(new WeekBlock(weekBlock.getWeekOf(), weekBlock.getBlock().getId()));
		return blockRepository.save(weekBlock.getBlock());
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
}
