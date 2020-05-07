package upcafe.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upcafe.dto.settings.PickupTime;
import upcafe.dto.settings.TimeBlockDTO;
import upcafe.entity.settings.PickupSettings;
import upcafe.repository.settings.PickupSettingsRepository;
import upcafe.utils.TimeUtils;

@Service
public class PickupTimesService {

	@Autowired
	private PickupSettingsRepository pickupRepository;

	@Autowired
	private CafeHoursService hoursService;

// 	public PickupSettings getPickupSettings() {
// 		return pickupRepository.findById("1").get();
// 	}

// 	public PickupSettings updatePickupSettings(PickupSettings settings) {
// 		return pickupRepository.save(settings);
// 	}

    private List<TimeBlockDTO> getSortedFutureBlocks() {
        List<TimeBlockDTO> allBlocksForTheDay = hoursService.getTimeBlocksForDay(LocalDate.now());
        List<TimeBlockDTO> futureBlocks = retrieveFutureBlocks(allBlocksForTheDay);

        // Sort the remaining time blocks in ascending time
        futureBlocks = futureBlocks.stream().sorted((a, b) -> {
            boolean result = a.getOpen().isBefore(b.getOpen());
            if (result)
                return -1;
            else
                return 1;
        }).collect(Collectors.toList());

        return futureBlocks;
    }

	public List<PickupTime> getAvailablePickupTimes() {

		List<PickupTime> pickupTimes = new ArrayList<PickupTime>();
		
        List<TimeBlockDTO> availableBlocks = getSortedFutureBlocks(); 
        

        PickupSettings pickupSettings = pickupRepository.findById("1").get();

        availableBlocks.forEach(block -> { 

            LocalTime runningTime = block.getOpen();

            // Check if we should add open time to the available pickup times
            if (pickupSettings.isPickupOnOpen() && runningTime.isBefore(
                    TimeUtils.getTimeNowWithIntervalPadding(pickupSettings.getIntervalBetweenPickupTimes()))) {
                
                pickupTimes.add(new PickupTime.Builder(block.getOpen()).build());
                runningTime = runningTime.plusMinutes(pickupSettings.getIntervalBetweenPickupTimes());
            }

            while (runningTime.isBefore(block.getClose())) {
            
                if (runningTime.isBefore(
                    TimeUtils.getTimeNowWithIntervalPadding(pickupSettings.getIntervalBetweenPickupTimes()))) {
            			runningTime = runningTime.plusMinutes(pickupSettings.getIntervalBetweenPickupTimes());
                } else {
                    pickupTimes.add(new PickupTime.Builder(runningTime).build());
                    runningTime = runningTime.plusMinutes(pickupSettings.getIntervalBetweenPickupTimes());
                }
            }
            
            if (pickupSettings.isPickupOnClose()) {
                pickupTimes.add(new PickupTime.Builder(block.getClose()).build());
            }
        });
            
        return pickupTimes;
    }

	private List<TimeBlockDTO> retrieveFutureBlocks(List<TimeBlockDTO> allBlocks) {
		return allBlocks.stream().filter(block -> {

			  return block.getClose().isAfter(TimeUtils.getTimeNow());

		}).collect(Collectors.toList());
	}
}
